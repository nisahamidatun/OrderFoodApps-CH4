package com.learning.orderfoodappsch3.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.learning.orderfoodappsch3.data.database.AppDatabase.Companion.getInstance
import com.learning.orderfoodappsch3.data.database.dao.CartDao
import com.learning.orderfoodappsch3.data.database.dao.OrderFoodDao
import com.learning.orderfoodappsch3.data.database.entity.CartEntity
import com.learning.orderfoodappsch3.data.database.entity.OrderFoodEntity
import com.learning.orderfoodappsch3.data.database.mapper.toOrderFoodEntity
import com.learning.orderfoodappsch3.data.sourcedata.OrderFoodDataSourceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Database(
    entities = [OrderFoodEntity::class, CartEntity::class],
    version = 1,
    exportSchema = true
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun orderfoodDao(): OrderFoodDao
    abstract fun cartDao(): CartDao

    companion object{
        private const val DB_NAME = "OrderFood.db"
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase {
            return INSTANCE?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .addCallback(
                        DatabaseSeederCallback(context)
                    )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class DatabaseSeederCallback(private val context: Context): RoomDatabase.Callback(){
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main+job)

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        scope.launch {
            getInstance(context).orderfoodDao().insertOrderFood(prepopulateOrderFood())
            getInstance(context).cartDao().insertChart(prepopulateCarts())
        }
    }

    private fun prepopulateOrderFood(): List<OrderFoodEntity>{
        return OrderFoodDataSourceImpl().getOrderFoods().toOrderFoodEntity()
    }

    private fun prepopulateCarts(): List<CartEntity> {
        return mutableListOf(
            CartEntity(
                id = 1,
                orderfoodId = 1,
                notes = "Pedas dan Fresh",
                quantityCartItem = 2
            )
        )
    }

}
