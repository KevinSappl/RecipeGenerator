import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductTitle(
    val de: String
) : Parcelable

@Parcelize
data class CategoryTitle(
    val de: String
) : Parcelable

@Parcelize
data class Category(
    val titles: CategoryTitle
) : Parcelable

@Parcelize
data class Weight(
    val value: Double,
    val unit: String
) : Parcelable

@Parcelize
data class Metadata(
    val generic: Generic
) : Parcelable

@Parcelize
data class Generic(
    val weight: List<Weight>
) : Parcelable

@Parcelize
data class Product(
    val titles: ProductTitle,
    val categories: List<Category>,
    val metadata: Metadata
) : Parcelable

@Parcelize
data class ApiResponse(
    val product: Product
) : Parcelable

@Parcelize
data class BarcodeResponseDataModel(
    val title: String,
    val category: String,
    val weight: String
) : Parcelable
