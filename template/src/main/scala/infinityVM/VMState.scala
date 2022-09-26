package infinityVM

final case class VMState(
  registers: Map[Int, Int],
  programCounter: Int,
  memory: Vector[Int],
  state: ExternalState,
)
