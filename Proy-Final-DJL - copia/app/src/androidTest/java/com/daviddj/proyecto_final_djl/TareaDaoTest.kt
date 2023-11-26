package com.daviddj.proyecto_final_djl

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.daviddj.proyecto_final_djl.data.TareaDAO
import com.daviddj.proyecto_final_djl.data.TareasDatabase
import com.daviddj.proyecto_final_djl.model.Tarea
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TareaDaoTest {
    private lateinit var tareaDAO: TareaDAO
    private lateinit var tareaDatabase: TareasDatabase
    private var tarea1= Tarea(1,"nota prueba 1", "descripcion prueba 1", "2023-11-11", false, "contenido prueba 1")
    private var tarea2= Tarea(2,"nota prueba 2", "descripcion prueba 2", "2023-11-11", false, "contenido prueba 2")

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        tareaDatabase = Room.inMemoryDatabaseBuilder(context, TareasDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        tareaDAO = tareaDatabase.tareaDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        tareaDatabase.close()
    }

    private suspend fun addOneItemToDb() {
        tareaDAO.insert(tarea1)
    }

    private suspend fun addTwoItemsToDb() {
        tareaDAO.insert(tarea1)
        tareaDAO.insert(tarea2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsItemIntoDB() = runBlocking {
        addOneItemToDb()
        val allItems = tareaDAO.getAllItems().first()
        assertEquals(allItems[0], tarea1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnsAllItemsFromDB() = runBlocking {
        addTwoItemsToDb()
        val allItems = tareaDAO.getAllItems().first()
        assertEquals(allItems[0], tarea1)
        assertEquals(allItems[1], tarea2)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetItem_returnsItemFromDB() = runBlocking {
        addOneItemToDb()
        val item = tareaDAO.getItem(1)
        assertEquals(item.first(), tarea1)
    }

//    @Test
//    @Throws(Exception::class)
//    fun daoDeleteItems_deletesAllItemsFromDB() = runBlocking {
//        addTwoItemsToDb()
//        tareaDAO.delete(tarea1)
//        tareaDAO.delete(tarea2)
//        val allItems = tareaDAO.getAllItems().first()
//        TestCase.assertTrue(allItems.isEmpty())
//    }
}