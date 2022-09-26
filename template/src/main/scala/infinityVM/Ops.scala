package infinityVM

enum Instruction:
  case HALT
  case InterruptRead(address: Int)
  case InterruptWrite(address: Int)
  case Zero(reg: Int)
  case AddI(reg: Int, amount: Int)
  case Eql(reg1: Int, reg2: Int, reg3: Int)
  case Jeq(reg1: Int, reg2: Int, addr: Int)
  case Jump(addr: Int)
  case Load(dataReg: Int, addrReg: Int)
  case Add(reg1: Int, reg2: Int, reg3: Int)
  case Mod(reg1: Int, reg2: Int, reg3: Int)
  case Div(reg1: Int, reg2: Int, reg3: Int)
  case Store(dataReg: Int, addrReg: Int)
  case NOP
