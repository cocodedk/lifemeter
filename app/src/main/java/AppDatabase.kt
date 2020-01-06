import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(TarotCard::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tarotCardDao(): TarotCardDao
}
