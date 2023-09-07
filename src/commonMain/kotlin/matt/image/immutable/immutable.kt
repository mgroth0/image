package matt.image.immutable

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer


@Serializable(with = ImmutableByteArray.Companion::class)
class ImmutableByteArray(bytes: ByteArray) {
    @OptIn(InternalSerializationApi::class)
    companion object : KSerializer<ImmutableByteArray> {
        private val byteArraySer by lazy {
            serializer<ByteArray>()
        }
        override val descriptor by lazy { byteArraySer.descriptor }

        override fun deserialize(decoder: Decoder): ImmutableByteArray {
            return ImmutableByteArray(decoder.decodeSerializableValue(byteArraySer))
        }

        override fun serialize(
            encoder: Encoder,
            value: ImmutableByteArray
        ) {
            encoder.encodeSerializableValue(byteArraySer, value.bytes)
        }

    }

    private val bytes = bytes.copyOf()

    fun copy() = bytes.copyOf()

}