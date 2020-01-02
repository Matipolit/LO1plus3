package pl.matmar.matipolit.lo1plus.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(
    entities = arrayOf(User::class, DatabaseCard::class, DatabaseGodziny::class, DatabaseGrades::class), version = 4, exportSchema = false)
//@TypeConverters(DateConverter::class)
abstract class LO1Database : RoomDatabase() {


    abstract val userDao : UserDao
    abstract val homeDao: HomeDao
    abstract val gradesDao: GradesDao
    companion object{

        var MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) { // Since we didn't alter the table, there's nothing else to do here.
            }
        }
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
                "user")
                .allowMainThreadQueries()
                .addMigrations(MIGRATION_2_3)
                .build()
    }
}


@Dao
interface UserDao{

    //update or insert = upsert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(user: User) : Long

    @Query("SELECT * FROM user WHERE databaseId = $CURRENT_USERDB_ID")
    fun getUser() : LiveData<User>

    @Query("DELETE FROM user WHERE databaseId = $CURRENT_USERDB_ID")
    fun deleteUser()

}

@Dao
interface HomeDao{

    @Query("select * from databasecard WHERE content != 'n/a'")
    fun getCards(): LiveData<List<DatabaseCard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCards(vararg cards: DatabaseCard)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertGodziny(godziny: DatabaseGodziny?)

    @Query("SELECT * FROM databasegodziny")
    fun getGodziny() : LiveData<DatabaseGodziny?>

}

@Dao
interface GradesDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(grades: DatabaseGrades)

    @Query("SELECT * FROM databasegrades")
    fun getGrades() : LiveData<DatabaseGrades>
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