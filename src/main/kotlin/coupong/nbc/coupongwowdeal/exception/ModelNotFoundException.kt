package coupong.nbc.coupongwowdeal.exception

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException

data class ModelNotFoundException(
    val modelName: String,
    val id: Long
) : NotFoundException() {
    override val message: String = "Model $modelName with given id $id not found"
}
