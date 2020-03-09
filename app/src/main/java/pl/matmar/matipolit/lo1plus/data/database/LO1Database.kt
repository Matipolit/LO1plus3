package pl.matmar.matipolit.lo1plus.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(
    entities = [User::class, DatabaseCard::class, DatabaseGodziny::class, DatabaseGrades::class, DatabasePlan::class, DatabaseAttendance::class, DatabasePlansLegend::class, DatabasePlansPlan::class], version = 7, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LO1Database : RoomDatabase() {


    abstract val userDao : UserDao
    abstract val homeDao: HomeDao
    abstract val gradesDao: GradesDao
    abstract val planDao : PlanDao
    abstract val attDao : AttDao
    abstract val plansDao : PlansDao

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

    @Query("SELECT * FROM user WHERE databaseId = $CURRENT_USER_ID LIMIT 1")
    fun getUser() : LiveData<User>

    @Query("DELETE FROM user WHERE databaseId = $CURRENT_USER_ID")
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

    @Query("SELECT * FROM databasegodziny LIMIT 1")
    fun getGodziny() : LiveData<DatabaseGodziny?>

}

@Dao
interface GradesDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(grades: DatabaseGrades)

    @Query("SELECT * FROM databasegrades LIMIT 1")
    fun getGrades() : LiveData<DatabaseGrades>
}

@Dao
interface PlanDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(plan: DatabasePlan)

    @Query("SELECT * FROM databaseplan WHERE timeInMilis = :timeMillis LIMIT 1")
    fun getPlan(timeMillis: Long) : DatabasePlan?

    @Query("DELETE FROM databaseplan WHERE timeInMilis < :timeMin AND timeInMilis > :timeMax")
    fun clearPlans(timeMin: Long, timeMax: Long)

}

@Dao
interface AttDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(attendance: DatabaseAttendance)

    @Query("SELECT * FROM databaseattendance WHERE timeInMilis = :timeMillis LIMIT 1")
    fun getAttendance(timeMillis: Long) : DatabaseAttendance?

    @Query("DELETE FROM databaseattendance WHERE timeInMilis < :timeMin AND timeInMilis > :timeMax")
    fun clearAtts(timeMin: Long, timeMax: Long)

}

@Dao
interface PlansDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertLegend(plansLegend: DatabasePlansLegend)

    @Query("SELECT * FROM DatabasePlansLegend LIMIT 1")
    fun getLegend() : LiveData<DatabasePlansLegend>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertPlan(plansPlan: DatabasePlansPlan)

    @Query("SELECT * FROM databaseplansplan WHERE `index` = :id LIMIT 1")
    fun getPlan(id: String) : DatabasePlansPlan?

    @Query("SELECT * FROM databaseplansplan")
    fun getAllPlans() : LiveData<List<DatabasePlansPlan>>

}