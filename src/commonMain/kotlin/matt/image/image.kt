package matt.image

import kotlinx.serialization.Serializable


interface Image
interface ImmutableImage : Image
interface Raster : Image

sealed interface ImmutableRaster : Raster, ImmutableImage

@Serializable
class Png(val bytes: ByteArray) : ImmutableRaster


interface VectorGraphic : Image





