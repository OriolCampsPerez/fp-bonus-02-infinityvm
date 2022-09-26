package infinityVM

import org.scalatest.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AnyFlatSpec
import Instruction.*
import ExternalState.*

class InfinityVMSpec extends AnyFlatSpec with Matchers with AppendedClues:
  import InfinityVMSpec.*

  // format: off

  "decodeInstruction" should "decode opcode 0 to HALT" in {
    decodeInstruction( 0, 0, 0, 0) should be (HALT) withClue wrongOpcode
    decodeInstruction( 0,14, 0, 0) should be (HALT) withClue ignoreBCD
    decodeInstruction( 0,14,58,23) should be (HALT) withClue ignoreBCD
    decodeInstruction( 0,14,58,23) should be (HALT) withClue ignoreBCD
  }

  it should "decode opcode 1 to InterruptRead" in {
    decodeInstruction( 1, 0, 0, 0) shouldBe a [InterruptRead] withClue wrongOpcode
    decodeInstruction( 1,14, 0, 0) should be(InterruptRead(14)) withClue address2nd
    decodeInstruction( 1,14,58,23) should be(InterruptRead(14)) withClue ignoreCD
    decodeInstruction( 1,14,58, 0) should be(InterruptRead(14)) withClue ignoreCD
  }

  it should "decode opcode 2 to InterruptWrite" in {
    decodeInstruction( 2, 0, 0, 0) shouldBe a[InterruptWrite] withClue wrongOpcode
    decodeInstruction( 2,14, 0, 0) should be(InterruptWrite(14)) withClue address2nd
    decodeInstruction( 2,14,58,23) should be(InterruptWrite(14)) withClue ignoreCD
    decodeInstruction( 2,14,58, 0) should be(InterruptWrite(14)) withClue ignoreCD
  }

  it should "decode opcode 3 to Zero" in {
    decodeInstruction( 3, 0, 0, 0) shouldBe a[Zero] withClue wrongOpcode
    decodeInstruction( 3,14, 0, 0) should be(Zero(14)) withClue reg2nd
    decodeInstruction( 3,14,58,23) should be(Zero(14)) withClue ignoreCD
    decodeInstruction( 3,14,58, 0) should be(Zero(14)) withClue ignoreCD
  }

  it should "decode opcode 4 to AddI" in {
    decodeInstruction( 4, 0, 0, 0) shouldBe a[AddI] withClue wrongOpcode
    decodeInstruction( 4,14, 0, 0) should be(AddI(14,0)) withClue reg2nd
    decodeInstruction( 4,14,58, 0) should be(AddI(14,58)) withClue value3rd
    decodeInstruction( 4,14,58,49) should be(AddI(14,58)) withClue ignoreD
  }

  it should "decode opcode 5 to Eql" in {
    decodeInstruction( 5, 0, 0, 0) shouldBe a[Eql] withClue wrongOpcode
    decodeInstruction( 5,14, 0, 0) should be(Eql(14,0,0)) withClue reg2nd1
    decodeInstruction( 5,14,58, 0) should be(Eql(14,58,0)) withClue reg3rd2
    decodeInstruction( 5,14,58,49) should be(Eql(14,58,49)) withClue reg4thRes
  }

  it should "decode opcode 6 to Jeq" in {
    decodeInstruction( 6, 0, 0, 0) shouldBe a[Jeq] withClue wrongOpcode
    decodeInstruction( 6,14, 0, 0) should be(Jeq(14,0,0)) withClue reg2nd1
    decodeInstruction( 6,14,58, 0) should be(Jeq(14,58,0)) withClue reg3rd2
    decodeInstruction( 6,14,58,49) should be(Jeq(14,58,49)) withClue address4thJump
  }

  it should "decode opcode 7 to Jump" in {
    decodeInstruction( 7, 0, 0, 0) shouldBe a[Jump] withClue wrongOpcode
    decodeInstruction( 7,14, 0, 0) should be(Jump(14)) withClue address2nd
    decodeInstruction( 7,14,58, 0) should be(Jump(14)) withClue ignoreCD
    decodeInstruction( 7,14,58,49) should be(Jump(14)) withClue ignoreCD
  }

  it should "decode opcode 8 to Load" in {
    decodeInstruction( 8, 0, 0, 0) shouldBe a[Load] withClue wrongOpcode
    decodeInstruction( 8,14, 0, 0) should be(Load(14,0)) withClue reg2nd
    decodeInstruction( 8,14,58, 0) should be(Load(14,58)) withClue address3rd
    decodeInstruction( 8,14,58,49) should be(Load(14,58)) withClue ignoreD
  }

  it should "decode opcode 9 to Add" in {
    decodeInstruction( 9, 0, 0, 0) shouldBe a[Add] withClue wrongOpcode
    decodeInstruction( 9,14, 0, 0) should be(Add(14,0,0)) withClue reg2nd1
    decodeInstruction( 9,14,58, 0) should be(Add(14,58,0)) withClue reg3rd2
    decodeInstruction( 9,14,58,49) should be(Add(14,58,49)) withClue reg4thRes
  }

  it should "decode opcode 1 0 to Mod" in {
    decodeInstruction(10, 0, 0, 0) shouldBe a[Mod] withClue wrongOpcode
    decodeInstruction(10,14, 0, 0) should be(Mod(14,0,0)) withClue reg2nd1
    decodeInstruction(10,14,58, 0) should be(Mod(14,58,0)) withClue reg3rd2
    decodeInstruction(10,14,58,49) should be(Mod(14,58,49)) withClue reg4thRes
  }

  it should "decode opcode 11 to Div" in {
    decodeInstruction(11, 0, 0, 0) shouldBe a[Div] withClue wrongOpcode
    decodeInstruction(11,14, 0, 0) should be(Div(14,0,0)) withClue reg2nd1
    decodeInstruction(11,14,58, 0) should be(Div(14,58,0)) withClue reg3rd2
    decodeInstruction(11,14,58,49) should be(Div(14,58,49)) withClue reg4thRes
  }

  it should "decode opcode 12 to Store" in {
    decodeInstruction(12, 0, 0, 0) shouldBe a[Store] withClue wrongOpcode
    decodeInstruction(12,14, 0, 0) should be(Store(14,0)) withClue reg2nd
    decodeInstruction(12,14,58, 0) should be(Store(14,58)) withClue address3rd
    decodeInstruction(12,14,58,49) should be(Store(14,58)) withClue ignoreD
  }

  it should "decode opcode 13 and higher to NOP" in {
    decodeInstruction(13, 0, 0, 0) should be (NOP) withClue wrongOpcode
    decodeInstruction(13,14, 0, 0) should be (NOP) withClue ignoreBCD
    decodeInstruction(13,14,58, 0) should be (NOP) withClue ignoreBCD
    decodeInstruction(13,14,58,49) should be (NOP) withClue ignoreBCD

    decodeInstruction(14,   0, 0, 0) should be (NOP) withClue invalidIsNop
    decodeInstruction(18,   0, 0, 0) should be (NOP) withClue invalidIsNop
    decodeInstruction(9999, 0, 0, 0) should be (NOP) withClue invalidIsNop
    decodeInstruction(-34,  0, 0, 0) should be (NOP) withClue invalidIsNop
  }

  // format: on

  /*
   * vmstate1 starts with memory of (0 to 30). So the parameters to any opcode
   * are always the following numbers (e.g. for opcode 4: b = 5, c = 6, d = 7)
   * The content of the memory matches its address.
   * The registers contain the indices 1 to 10, with the value being index + 1
   */

  "The execution of an instruction" should "halt the machine, if the Opcode is HALT" in {
    val newState = executeInstruction(vmstate1)
    newState.state should be(Halted) withClue readOpcodeWrong

    newState.memory should be(vmstate1.memory) withClue noMemoryChanges
    newState.programCounter should be(vmstate1.programCounter) withClue ", HALT should not affect program counter"
    newState.registers should be(vmstate1.registers) withClue noRegisterChanges
  }

  it should "evaluate a read interrupt on opcode 1" in {
    val oldState = vmstate1.copy(programCounter = 1)
    val newState = executeInstruction(oldState)
    newState.programCounter should be(oldState.programCounter + 4) withClue counterClue

    newState.state shouldBe a[ReadRequested] withClue readOpcodeWrong
    newState.state should be(ReadRequested(2)) withClue ", read address wrong"

    newState.memory should be(oldState.memory) withClue noMemoryChanges
    newState.registers should be(oldState.registers) withClue noRegisterChanges
  }

  it should "evaluate a write interrupt on opcode 2" in {
    val oldState = vmstate1.copy(programCounter = 2)
    val newState = executeInstruction(oldState)
    newState.programCounter should be(oldState.programCounter + 4) withClue counterClue

    newState.state shouldBe a[WriteRequested] withClue readOpcodeWrong
    newState.state should be(WriteRequested(3)) withClue ", write address wrong"

    newState.memory should be(oldState.memory) withClue noMemoryChanges
    newState.registers should be(oldState.registers) withClue noRegisterChanges
  }

  it should "zero the correct register on opcode 3" in {
    val oldState = vmstate1.copy(programCounter = 3)
    val newState = executeInstruction(oldState)
    newState.programCounter should be(oldState.programCounter + 4) withClue counterClue

    val zeroed = newState.registers.filter(_._2 == 0)
    zeroed.size should be <= 1 withClue ", only one register should be set to zero"

    if (newState.registers.contains(4)) newState.registers(4) should be(0)

    newState.state should be(Running) withClue stateRunning
    newState.memory should be(oldState.memory) withClue noMemoryChanges
  }

  it should "increment the correct register on opcode 4" in {
    val oldState = vmstate1.copy(programCounter = 4)
    val newState = executeInstruction(oldState)
    newState.programCounter should be(oldState.programCounter + 4) withClue counterClue

    newState.registers(5) should be(vmstate1.registers(5) + 6)

    newState.memory should be(oldState.memory) withClue noMemoryChanges
    newState.state should be(Running) withClue stateRunning
  }

  it should "compare two registers and write the result on opcode 5" in {
    val oldState = vmstate1.copy(programCounter = 5)
    val newState = executeInstruction(oldState)
    newState.programCounter should be(oldState.programCounter + 4) withClue counterClue

    newState.registers(8) should be(-1) withClue ", first register smaller should give result -1"

    val newStateEq = executeInstruction(oldState.copy(registers = oldState.registers.updated(7,7)))
    newStateEq.registers(8) should be(0) withClue ", equal registers should give result 0"

    val newStateLt = executeInstruction(oldState.copy(registers = oldState.registers.updated(7,4)))
    newStateLt.registers(8) should be(1) withClue ", first register larger should give result -1"

    newState.memory should be(oldState.memory) withClue noMemoryChanges
    newState.state should be(Running) withClue stateRunning
  }

  it should "compare two registers and jump if equal on opcode 6" in {
    val oldState = vmstate1.copy(programCounter = 6)
    val newState = executeInstruction(oldState)
    val newStateEq = executeInstruction(oldState.copy(registers = Map(7 -> 42, 8 -> 42)))

    newState.programCounter should be(oldState.programCounter + 4) withClue (
      ", should do normal PC increment, if registers are not equal")

    newStateEq.programCounter should be(9) withClue (
      ", should set PC to jump address, if registers are equal")


    newState.memory should be(oldState.memory) withClue noMemoryChanges
    newState.registers should be(oldState.registers) withClue noRegisterChanges
    newState.state should be(Running) withClue stateRunning
  }

  it should "jump to given address on opcode 7" in {
    val oldState = vmstate1.copy(programCounter = 7)
    val newState = executeInstruction(oldState)

    newState.programCounter should be(8) withClue (
      ", should set PC to jump address")

    newState.memory should be(oldState.memory) withClue noMemoryChanges
    newState.registers should be(oldState.registers) withClue noRegisterChanges
    newState.state should be(Running) withClue stateRunning
  }

  it should "load from memory to register on opcode 8" in {
    val oldState = vmstateLoad
    val newState = executeInstruction(oldState)
    newState.programCounter should be(oldState.programCounter + 4) withClue counterClue

    println(newState)
    newState.registers(oldState.memory(1)) should be(newState.memory(newState.registers(oldState.memory(2)))) withClue indirectionClue

    newState.memory should be(oldState.memory) withClue noMemoryChanges
    newState.state should be(Running) withClue stateRunning
  }

  it should "add two registers on opcode 9" in {
    val opcode = 9
    val oldState = vmstate1.copy(programCounter = opcode)
    val newState = executeInstruction(oldState)
    newState.programCounter should be(oldState.programCounter + 4) withClue counterClue

    newState.registers(opcode + 3) should be(newState.registers(opcode + 1) + newState.registers(opcode + 2))

    newState.memory should be(oldState.memory) withClue noMemoryChanges
    newState.state should be(Running) withClue stateRunning

    val oldState2 = vmstate1.copy(memory = Vector(opcode,1,1,1))
    val newState2 = executeInstruction(oldState2)
    newState2.registers(1) should be (2 + 2) withClue (
      ", should work with result register being one of input registers")
  }

  it should "calculate the modulo of two registers on opcode 10" in {
    val opcode = 10
    val oldState = vmstate1.copy(programCounter = opcode)
    val newState = executeInstruction(oldState)
    newState.programCounter should be(oldState.programCounter + 4) withClue counterClue

    newState.registers(opcode + 3) should be(newState.registers(opcode + 1) % newState.registers(opcode + 2))

    newState.memory should be(oldState.memory) withClue noMemoryChanges
    newState.state should be(Running) withClue stateRunning

    val oldState2 = vmstate1.copy(memory = Vector(opcode,1,1,1))
    val newState2 = executeInstruction(oldState2)
    newState2.registers(1) should be (2 % 2) withClue (
      ", should work with result register being one of input registers")
  }

  it should "divide two registers on opcode 11" in {
    val opcode = 11
    val oldState = vmstate1.copy(programCounter = opcode, registers = Map(12 -> 30, 13 -> 3, 14 -> 99))
    val newState = executeInstruction(oldState)
    newState.programCounter should be(oldState.programCounter + 4) withClue counterClue

    newState.registers(opcode + 3) should be(10)

    newState.memory should be(oldState.memory) withClue noMemoryChanges
    newState.state should be(Running) withClue stateRunning

    val oldState2 = vmstate1.copy(memory = Vector(opcode,1,1,1))
    val newState2 = executeInstruction(oldState2)
    newState2.registers(1) should be (2 / 2) withClue (
      ", should work with result register being one of input registers")
  }
  

  it should "store from register to memory on opcode 12" in {
    val oldState = vmstateStore
    val newState = executeInstruction(oldState)
    newState.programCounter should be(oldState.programCounter + 4) withClue counterClue

    newState.memory(newState.registers(oldState.memory(2))) should be(newState.registers(oldState.memory(1))) withClue indirectionClue

    newState.state should be(Running) withClue stateRunning
  }
 

object InfinityVMSpec:
  val vmstate1 = VMState(
    ((1 to 16) zip (2 to 17)).toMap,
    0,
    (0 to 30).toVector,
    Running
  )

  val vmstateLoad = VMState(
    ((1 to 16) zip (2 to 17)).toMap,
    0,
    //         addrReg     target
    //           ↓           ↓
    Vector(8, 1, 4, 99, 77, 42),
    //        ↑
    //        reg, should become 42
    Running
  )

  val vmstateStore = VMState(
    ((1 to 16) zip (2 to 17)).toMap,
    0,
    //          addrReg     target, should become 2
    //            ↓           ↓
    Vector(12, 1, 4, 99, 77, 42),
    //         ↑
    //         reg, contains 2, should be stored
    Running
  )

  val counterClue = ", program counter should increase by instruction size (4)"
  val indirectionClue = ", are you correctly resolving the memory address? Second parameter is a *register*, containing the *memory* address."

  val readOpcodeWrong = ", are you reading the opcode from the right place?"
  val noMemoryOrRegisterChanges =
    ", memory or registers should not be changed here"
  val noMemoryChanges = ", memory should not be changed here"
  val noRegisterChanges = ", registers should not be changed here"

  val stateRunning = ", vm should be running after this operation"
  val wrongOpcode = ", wrong Opcode"
  val ignoreBCD = ", second to fourth parameter should be ignored"
  val ignoreCD = ", third and fourth parameter should be ignored"
  val ignoreD = ", fourth parameter should be ignored"

  val address2nd = ", address should match second parameter"
  val address3rd = ", address should match third parameter"
  val reg2nd = ", register should match second parameter"
  val value3rd = ", value should match third parameter"
  val reg2nd1 = ", first register should match second parameter"
  val reg3rd2 = ", second register should match third parameter"
  val reg4thRes = ", result register should match fourth parameter"
  val address4thJump = ", jump address should match fourth parameter"
  val invalidIsNop = ", any invalid opcode should decode to NOP"
