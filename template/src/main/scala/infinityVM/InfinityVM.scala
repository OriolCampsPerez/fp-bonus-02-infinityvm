package infinityVM

// Student: Oriol Camps Pérez
import Instruction.*
import ExternalState.*

import scala.annotation.tailrec

/* You may use the type classes and methods provided by the cats library */
import cats.*
import cats.data.{Store => _, *} // don't import cats.data.Store, because it conflicts with our "Store" opcode, and you won't need it
import cats.implicits.given

def decodeInstruction(a: Int, b: Int, c: Int, d: Int): Instruction = a match
  case 0 => HALT
  case 1 => InterruptRead(/*address=*/ b)
  case 2 => InterruptWrite(/*address=*/ b)
  case 3 => Zero(/*reg=*/ b)
  case 4 => AddI(/*reg=*/ b, /*amount=*/ c)
  case 5 => Eql(/*reg1=*/ b, /*reg2=*/ c, /*reg3=*/ d)
  case 6 => Jeq(/*reg1=*/ b, /*reg2=*/ c, /*address=*/ d)
  case 7 => Jump(/*address=*/ b)
  case 8 => Load(/*dataReg=*/ b, /*addressReg=*/ c)
  case 9 => Add(/*reg1=*/ b, /*reg2=*/ c, /*reg3=*/ d)
  case 10 => Mod(/*reg1=*/ b, /*reg2=*/ c, /*reg3=*/ d)
  case 11 => Div(/*reg1=*/ b, /*reg2=*/ c, /*reg3=*/ d)
  case 12 => Store(/*dataReg=*/ b, /*addressReg=*/ c)
  case 13 => NOP // redundant but more comprehensible
  case _ => NOP



def executeInstruction(vm: VMState): VMState =

//  def _nxtInstPoint(vm: VMState): Int = vm.programCounter + (1 * 4) // pointing next instruction

  def _getRegisterVal(vm: VMState, k: Int): Int = vm.registers.get(k) match
    case None => 0
    case Some(vl) => vl


  // //// // //// INSTRUCTIONS //// // //// //

  def _HALT(vm: VMState): VMState = vm.copy(state = Halted)

  def _InterruptRead(vm: VMState, mAddress: Int): VMState = vm.copy(
    state = ReadRequested(mAddress),
    programCounter = vm.programCounter + (1 * 4) // pointing next instruction
    )

  def _InterruptWrite(vm: VMState, mAddress: Int): VMState = vm.copy(
    state = WriteRequested(mAddress),
    programCounter = vm.programCounter + (1 * 4) // pointing next instruction
  )

  def _Zero(vm: VMState, address: Int): VMState =
    vm.copy(
    registers = vm.registers + (address -> 0),
    programCounter = vm.programCounter + (1 * 4) // pointing next instruction
    )

  def _AddI(vm: VMState, reg: Int, amount: Int): VMState =
    vm.copy(
      registers = vm.registers.get(reg) match
        case None    => vm.registers + ( reg -> (0 + amount) )
        case Some(v) => vm.registers + ( reg -> (v + amount) )
      ,
      programCounter = vm.programCounter + (1 * 4), // pointing next instruction
    )

  def _Eql(vm: VMState, reg1: Int, reg2: Int, rReg: Int): VMState =
    vm.copy(
      registers =
        if _getRegisterVal(vm, reg1) > _getRegisterVal(vm, reg2) then       vm.registers + (rReg ->  1)
        else if _getRegisterVal(vm, reg1) == _getRegisterVal(vm, reg2) then vm.registers + (rReg ->  0)
        else                                                                vm.registers + (rReg -> -1)
      ,
      programCounter = vm.programCounter + (1 * 4), // pointing next instruction
    )

  def _Jeq(vm: VMState, reg1: Int, reg2: Int, jAddress: Int): VMState =
    vm.copy(
      programCounter =
        if _getRegisterVal(vm, reg1) == _getRegisterVal(vm, reg2) then jAddress
        else vm.programCounter + (1 * 4) // pointing next instruction
      ,
    )

  def _Jump(vm: VMState, address: Int): VMState =
    vm.copy(
      programCounter = address
    )

  def _Load(vm: VMState, to_regK: Int, from_memAddress: Int): VMState =
    vm.copy(
      vm.memory.get(_getRegisterVal(vm, from_memAddress)) match
        case None =>    vm.registers + (to_regK -> 0)
        case Some(v) => vm.registers + (to_regK -> v)
      ,
      programCounter = vm.programCounter + (1 * 4), // pointing next instruction
    )

  def _Add(vm: VMState, reg1: Int, reg2: Int, rReg: Int): VMState =
    vm.copy(
      registers = vm.registers + (rReg -> ( _getRegisterVal(vm, reg1) + _getRegisterVal(vm, reg2) )),
      programCounter = vm.programCounter + (1 * 4), // pointing next instruction
    )

  def _Mod(vm: VMState, reg1: Int, reg2: Int, rReg: Int): VMState =
    vm.copy(
      registers = vm.registers + (rReg -> ( _getRegisterVal(vm, reg1) % _getRegisterVal(vm, reg2) )),
      programCounter = vm.programCounter + (1 * 4), // pointing next instruction
    )

  def _Div(vm: VMState, reg1: Int, reg2: Int, rReg: Int): VMState =
    vm.copy(
      registers = vm.registers + (rReg -> ( _getRegisterVal(vm, reg1) / _getRegisterVal(vm, reg2) )),
      programCounter = vm.programCounter + (1 * 4), // pointing next instruction
    )

  def _Store(vm: VMState, from_regK: Int, to_memAddress: Int): VMState =
    // https://dotty.epfl.ch/api/scala/collection/immutable/Vector.html
    @tailrec
    def _updatedMem(vm: VMState, to_memAddress: Int, data: Int): Vector[Int] =
      /* idea to "init" memory with 0 (middle addresses) when address is used for the 1st time --> directly in _updatedMem
      def _init_edZMem(mem: Vector[Int], targetAddress: Int): Vector[Int] =
        if targetAddress < mem.length then mem
        else __init_edZMem(mem.appended(0)) */
      if to_memAddress < vm.memory.length then vm.memory.updated(to_memAddress, data)
      else _updatedMem(vm.copy(memory = vm.memory.appended(0)), to_memAddress, data)

    vm.copy(
      programCounter = vm.programCounter + (1 * 4), // pointing next instruction
      memory = _updatedMem(vm, _getRegisterVal(vm, to_memAddress), _getRegisterVal(vm, from_regK))
    )

  def _NOP(vm: VMState): VMState =
    vm.copy(
      programCounter = vm.programCounter + (1 * 4), // pointing next instruction
    )

  // //// // //// //// // //// //// // //// //

  decodeInstruction(
    /* a= */ vm.memory(vm.programCounter + 0),
    /* b= */ vm.memory(vm.programCounter + 1),
    /* c= */ vm.memory(vm.programCounter + 2),
    /* d= */ vm.memory(vm.programCounter + 3)
  ) match
    case HALT => _HALT(vm)
    case InterruptRead(mAddress) => _InterruptRead(vm, mAddress)
    case InterruptWrite(mAddress) => _InterruptWrite(vm, mAddress)
    case Zero(address) => _Zero(vm, address)
    case AddI(reg, amount) => _AddI(vm, reg, amount)
    case Eql(reg1, reg2, rReg) => _Eql(vm, reg1, reg2, rReg)
    case Jeq(reg1, reg2, jAddress) => _Jeq(vm, reg1, reg2, jAddress)
    case Jump(address) => _Jump(vm, address)
    case Load(to_regK, from_memAddress) => _Load(vm, to_regK, from_memAddress)
    case Add(reg1, reg2, rReg) => _Add(vm, reg1, reg2, rReg)
    case Mod(reg1, reg2, rReg) => _Mod(vm, reg1, reg2, rReg)
    case Div(reg1, reg2, rReg) => _Div(vm, reg1, reg2, rReg)
    case Store(from_regK, to_memAddress) => _Store(vm, from_regK, to_memAddress)
    case NOP => _NOP(vm)
