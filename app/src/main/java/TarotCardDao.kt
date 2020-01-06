import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TarotCardDao {
    @Query("SELECT * FROM tarotCard")
    fun getAll(): List<TarotCard>

    @Query("SELECT * FROM tarotCard WHERE uid IN (:tarotCardIds)")
    fun loadAllByIds(tarotCardIds: IntArray): List<TarotCard>

    @Query("SELECT * FROM tarotCard WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): TarotCard

    @Insert
    fun insertAll(tarotCards: TarotCard)

    @Delete
    fun delete(tarotCard: TarotCard)
}