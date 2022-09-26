package infinityVM

enum ExternalState:
  case ReadRequested(address: Int)
  case WriteRequested(address: Int)
  case Halted
  case Running
