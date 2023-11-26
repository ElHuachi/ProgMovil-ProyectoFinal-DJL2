package com.daviddj.proyecto_final_djl

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.daviddj.proyecto_final_djl.data.NotaDAO
import com.daviddj.proyecto_final_djl.data.NotasDatabase
import com.daviddj.proyecto_final_djl.data.TareasDatabase
import com.daviddj.proyecto_final_djl.model.Nota
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class NotaDaoTest {
    private lateinit var notaDAO: NotaDAO
    private lateinit var notaDatabase: NotasDatabase
    private var nota1 = Nota(1,"nota prueba 1","desc 1","2023-11-11","cont 1")
    private var nota2 = Nota(2,"nota prueba 2","desc 2","2023-11-11","cont 2")

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        notaDatabase = Room.inMemoryDatabaseBuilder(context, NotasDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        notaDAO = notaDatabase.notaDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        notaDatabase.close()
    }

    private suspend fun addOneItemToDb() {
        notaDAO.insert(nota1)
    }

    private suspend fun addTwoItemsToDb() {
        notaDAO.insert(nota1)
        notaDAO.insert(nota2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsItemIntoDB() = runBlocking {
        addOneItemToDb()
        val allItems = notaDAO.getAllItems().first()
        TestCase.assertEquals(allItems[0], nota1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnsAllItemsFromDB() = runBlocking {
        addTwoItemsToDb()
        val allItems = notaDAO.getAllItems().first()
        TestCase.assertEquals(allItems[0], nota1)
        TestCase.assertEquals(allItems[1], nota2)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetItem_returnsItemFromDB() = runBlocking {
        addOneItemToDb()
        val item = notaDAO.getItem(1)
        assertEquals(item.first(), nota1)
    }

//    @Test
//    @Throws(Exception::class)
//    fun daoDeleteItems_deletesAllItemsFromDB() = runBlocking {
//        addTwoItemsToDb()
//        notaDAO.delete(nota1)
//        notaDAO.delete(nota2)
//        val allItems = notaDAO.getAllItems().first()
//        assertTrue(allItems.isEmpty())
//    }
}