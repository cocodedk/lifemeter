import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TarotCard(
    @PrimaryKey val uid: Int,
    @ColumnInfo (name = "name") val name: String,
    @ColumnInfo (name = "description") val description: String
)