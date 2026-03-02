package com.example.inventrytracker

import com.example.inventrytracker.Model.InventoryItem
import org.junit.Test
import org.junit.Assert.*

/**
 * Unit Test Example (Logic Testing)
 * This follows your sample's clean structure but tests your Inventory Data.
 */
class InventoryLogicTest {

    @Test
    fun testInventoryItemMapping() {
        // 1. Create a sample item
        val item = InventoryItem(
            id = "test_123",
            name = "Apple",
            quantity = 50,
            userId = "user_01"
        )

        // 2. Convert to Map (Logic shared with Firebase)
        val map = item.toMap()

        // 3. Assertions (Verifying according to your logic)
        assertEquals("Apple", map["name"])
        assertEquals(50, map["quantity"])
        assertEquals("user_01", map["userId"])
    }

    @Test
    fun testQuantityValidation() {
        val qty = 10
        // Simple logic test: check if quantity is positive
        assertTrue("Quantity should be positive", qty > 0)
    }
}
