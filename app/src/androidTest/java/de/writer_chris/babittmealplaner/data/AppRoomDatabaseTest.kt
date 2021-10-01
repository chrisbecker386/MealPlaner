package de.writer_chris.babittmealplaner.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4

import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


class AppRoomDatabaseTest : TestCase() {

    private lateinit var db: AppRoomDatabase
    private lateinit var dao: DishDao

    @Before
    override fun setUp() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, AppRoomDatabase::class.java).build()
        dao = db.dishDao()
    }

    @Test
    fun testInsertAnEntryAndReadItInDb() = runBlocking {
        val dish = Dish(dishName = "Soup")
        dao.insert(dish)
        var result = dao.getDishWithoutFlow(dish.dishName)

        assertThat(result.dishName == dish.dishName).isTrue()
    }


    @After
    fun closeDb() {
        db.close()
    }


}
