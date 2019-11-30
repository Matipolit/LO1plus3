package pl.matmar.matipolit.lo1plus.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Database(
    entities = [User::class],
    version = 2
)

abstract class LO1Database : RoomDatabase() {

    abstract val userDao : UserDao
    companion object{
        @Volatile
        private var INSTANCE : LO1Database? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK) {
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                LO1Database::class.java,
                "user").build()
    }
}


@Dao
interface UserDao{

    //update or insert = upsert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(user: User) : Long

    @Query("SELECT * FROM user WHERE databaseId = $CURRENT_USERDB_ID")
    fun getUser() : LiveData<User>
}

/*private lateinit var INSTANCE: UserDatabase

fun getDatabase(context: Context): UserDatabase {
    synchronized(UserDatabase::class.java) {
        if(!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                UserDatabase::class.java,
                "user").build()
        }
    }
    return INSTANCE
}*/