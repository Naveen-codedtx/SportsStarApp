package com.example.myapp

import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.After
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Comprehensive unit test suite for MyApp Android application.
 * 
 * Testing Framework: JUnit 4 (as configured in build.gradle.kts)
 * This test suite provides extensive coverage of core Kotlin functionality,
 * data structures, mathematical operations, string manipulations, and
 * Android-specific patterns that would be commonly used in the application.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    
    private lateinit var testData: MutableList<Int>
    private lateinit var testMap: MutableMap<String, Any>
    private lateinit var userProfiles: MutableList<UserProfile>
    
    // Data class for testing Android-like user data
    data class UserProfile(
        val id: Int,
        val name: String,
        val email: String,
        val isActive: Boolean = true
    ) {
        fun getDisplayName(): String = if (name.isBlank()) "Anonymous User" else name
        fun isValidEmail(): Boolean = email.contains("@") && email.contains(".")
    }
    
    // Enum for testing state management patterns common in Android
    enum class NetworkState {
        IDLE, LOADING, SUCCESS, ERROR
    }
    
    // Sealed class for testing result handling patterns
    sealed class ApiResult<T> {
        data class Success<T>(val data: T) : ApiResult<T>()
        data class Error<T>(val message: String, val code: Int = -1) : ApiResult<T>()
        object Loading : ApiResult<Nothing>()
    }
    
    @Before
    fun setUp() {
        testData = mutableListOf(1, 2, 3, 4, 5)
        testMap = mutableMapOf(
            "string" to "test",
            "number" to 42,
            "boolean" to true
        )
        userProfiles = mutableListOf(
            UserProfile(1, "Alice Johnson", "alice@example.com"),
            UserProfile(2, "Bob Smith", "bob@test.com", false),
            UserProfile(3, "", "invalid-email")
        )
    }
    
    @After
    fun tearDown() {
        testData.clear()
        testMap.clear()
        userProfiles.clear()
    }
    
    // Original test maintained for backwards compatibility
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    
    // Comprehensive arithmetic operations tests
    @Test
    fun arithmetic_addition_allScenarios() {
        assertEquals(0, 5 + (-5))
        assertEquals(-3, (-1) + (-2))
        assertEquals(2, (-3) + 5)
        assertEquals(5, 5 + 0)
        assertEquals(0, 0 + 0)
        assertEquals(-5, (-5) + 0)
    }
    
    @Test
    fun arithmetic_subtraction_allScenarios() {
        assertEquals(2, 4 - 2)
        assertEquals(-2, 2 - 4)
        assertEquals(0, 5 - 5)
        assertEquals(8, 3 - (-5))
        assertEquals(-8, (-3) - 5)
    }
    
    @Test
    fun arithmetic_multiplication_allScenarios() {
        assertEquals(6, 2 * 3)
        assertEquals(0, 0 * 5)
        assertEquals(-6, (-2) * 3)
        assertEquals(6, (-2) * (-3))
        assertEquals(25, 5 * 5)
        assertEquals(0, 0 * 0)
    }
    
    @Test
    fun arithmetic_division_allScenarios() {
        assertEquals(2, 6 / 3)
        assertEquals(0, 0 / 5)
        assertEquals(-2, (-6) / 3)
        assertEquals(2, (-6) / (-3))
        assertEquals(1, 7 / 7)
    }
    
    @Test(expected = ArithmeticException::class)
    fun arithmetic_divisionByZero_shouldThrowException() {
        @Suppress("UNUSED_VARIABLE", "DIVISION_BY_ZERO")
        val result = 5 / 0
    }
    
    // String manipulation tests for Android UI text processing
    @Test
    fun string_concatenation_variousFormats() {
        val str1 = "Hello"
        val str2 = "World"
        assertEquals("HelloWorld", str1 + str2)
        assertEquals("Hello World", "$str1 $str2")
        assertEquals("Hello, World!", "$str1, $str2!")
        assertEquals("Greeting: Hello World", "Greeting: $str1 $str2")
    }
    
    @Test
    fun string_validation_methods() {
        assertEquals(5, "Hello".length)
        assertEquals(0, "".length)
        assertEquals(1, " ".length)
        assertEquals(13, "Hello, World!".length)
        
        assertTrue("".isEmpty())
        assertFalse("Hello".isEmpty())
        assertFalse(" ".isEmpty())
        
        assertTrue("".isBlank())
        assertTrue("   ".isBlank())
        assertTrue("\t\n\r".isBlank())
        assertFalse("Hello".isBlank())
        assertFalse(" Hello ".isBlank())
    }
    
    @Test
    fun string_manipulation_operations() {
        val text = "Hello World"
        assertEquals("Hello", text.substring(0, 5))
        assertEquals("World", text.substring(6))
        assertEquals("", text.substring(5, 5))
        assertEquals("llo", text.substring(2, 5))
        
        assertEquals("Hello", "  Hello  ".trim())
        assertEquals("", "   ".trim())
        assertEquals("Hello World", "\tHello World\n".trim())
        
        assertEquals("Hi World", text.replace("Hello", "Hi"))
        assertEquals("Hello Universe", text.replace("World", "Universe"))
        assertEquals("Hello World", text.replace("xyz", "abc"))
    }
    
    @Test(expected = StringIndexOutOfBoundsException::class)
    fun string_substring_invalidIndex_shouldThrowException() {
        "Hello".substring(10)
    }
    
    // Collection operations for data management
    @Test
    fun list_basicOperations() {
        val list = mutableListOf<Int>()
        list.add(1)
        list.add(2)
        list.addAll(listOf(3, 4, 5))
        
        assertEquals(5, list.size)
        assertEquals(1, list[0])
        assertEquals(5, list[4])
        assertTrue(list.contains(3))
        assertFalse(list.contains(10))
    }
    
    @Test
    fun list_removalOperations() {
        val initialSize = testData.size
        assertTrue(testData.remove(3))
        assertEquals(initialSize - 1, testData.size)
        assertFalse(testData.contains(3))
        assertFalse(testData.remove(99)) // Non-existent element
        
        testData.removeAt(0)
        assertEquals(initialSize - 2, testData.size)
        assertEquals(2, testData[0]) // First element should now be 2
    }
    
    @Test
    fun list_functionalOperations() {
        val evenNumbers = testData.filter { it % 2 == 0 }
        assertEquals(2, evenNumbers.size)
        assertTrue(evenNumbers.contains(2))
        assertTrue(evenNumbers.contains(4))
        
        val doubled = testData.map { it * 2 }
        assertEquals(listOf(2, 4, 6, 8, 10), doubled)
        
        val sum = testData.reduce { acc, n -> acc + n }
        assertEquals(15, sum)
        
        val sumWithInitial = testData.fold(10) { acc, n -> acc + n }
        assertEquals(25, sumWithInitial)
        
        assertTrue(testData.all { it > 0 })
        assertTrue(testData.any { it > 3 })
        assertFalse(testData.all { it > 3 })
    }
    
    @Test(expected = UnsupportedOperationException::class)
    fun list_reduce_emptyList_shouldThrowException() {
        emptyList<Int>().reduce { acc, n -> acc + n }
    }
    
    // Map operations for key-value storage
    @Test
    fun map_basicOperations() {
        val map = mutableMapOf<String, Int>()
        map["key1"] = 100
        map["key2"] = 200
        map.putAll(mapOf("key3" to 300, "key4" to 400))
        
        assertEquals(4, map.size)
        assertEquals(100, map["key1"])
        assertEquals(400, map["key4"])
        assertNull(map["nonexistent"])
        
        assertTrue(map.containsKey("key1"))
        assertTrue(map.containsValue(200))
        assertFalse(map.containsKey("missing"))
    }
    
    @Test
    fun map_collectionViews() {
        val keys = testMap.keys.toList()
        assertEquals(3, keys.size)
        assertTrue(keys.contains("string"))
        assertTrue(keys.contains("number"))
        assertTrue(keys.contains("boolean"))
        
        val values = testMap.values.toList()
        assertEquals(3, values.size)
        assertTrue(values.contains("test"))
        assertTrue(values.contains(42))
        assertTrue(values.contains(true))
    }
    
    // Null safety tests - critical for Android development
    @Test
    fun nullSafety_safeCallOperator() {
        val nullString: String? = null
        val nonNullString: String? = "Hello"
        
        assertNull(nullString?.length)
        assertEquals(5, nonNullString?.length)
        assertNull(nullString?.uppercase())
        assertEquals("HELLO", nonNullString?.uppercase())
    }
    
    @Test
    fun nullSafety_elvisOperator() {
        val nullString: String? = null
        val nonNullString: String? = "Hello"
        
        assertEquals("Default", nullString ?: "Default")
        assertEquals("Hello", nonNullString ?: "Default")
        assertEquals(0, nullString?.length ?: 0)
        assertEquals(5, nonNullString?.length ?: 0)
    }
    
    @Test(expected = KotlinNullPointerException::class)
    fun nullSafety_notNullAssertion_shouldThrowOnNull() {
        val nullString: String? = null
        @Suppress("UNUSED_VARIABLE")
        val length = nullString!!.length
    }
    
    @Test
    fun nullSafety_scopeFunctions() {
        val nullString: String? = null
        val nonNullString: String? = "Hello"
        
        var executed = false
        nullString?.let { executed = true }
        assertFalse(executed)
        
        nonNullString?.let { executed = true }
        assertTrue(executed)
        
        val result = nonNullString?.let { it.uppercase() }
        assertEquals("HELLO", result)
    }
    
    // Mathematical operations for calculations
    @Test
    fun math_advancedOperations() {
        assertEquals(2.0, sqrt(4.0), 0.001)
        assertEquals(0.0, sqrt(0.0), 0.001)
        assertEquals(3.0, sqrt(9.0), 0.001)
        assertTrue(sqrt(-1.0).isNaN())
        
        assertEquals(5, abs(-5))
        assertEquals(5, abs(5))
        assertEquals(0, abs(0))
        assertEquals(3.14, abs(-3.14), 0.001)
    }
    
    @Test
    fun math_integerBoundaryConditions() {
        val maxInt = Int.MAX_VALUE
        val overflowed = maxInt + 1
        assertEquals(Int.MIN_VALUE, overflowed)
        
        val minInt = Int.MIN_VALUE
        val underflowed = minInt - 1
        assertEquals(Int.MAX_VALUE, underflowed)
    }
    
    // Boolean logic operations
    @Test
    fun boolean_logicalOperations() {
        assertTrue(true && true)
        assertFalse(true && false)
        assertFalse(false && true)
        assertFalse(false && false)
        
        assertTrue(true || true)
        assertTrue(true || false)
        assertTrue(false || true)
        assertFalse(false || false)
        
        assertTrue(!false)
        assertFalse(!true)
    }
    
    // Range operations for iteration and bounds checking
    @Test
    fun range_containsOperations() {
        val range = 1..10
        assertTrue(5 in range)
        assertFalse(0 in range)
        assertFalse(11 in range)
        assertTrue(1 in range)
        assertTrue(10 in range)
    }
    
    @Test
    fun range_iterationPatterns() {
        val forwardResult = mutableListOf<Int>()
        for (i in 1..3) {
            forwardResult.add(i)
        }
        assertEquals(listOf(1, 2, 3), forwardResult)
        
        val backwardResult = mutableListOf<Int>()
        for (i in 3 downTo 1) {
            backwardResult.add(i)
        }
        assertEquals(listOf(3, 2, 1), backwardResult)
        
        val stepResult = mutableListOf<Int>()
        for (i in 1..10 step 2) {
            stepResult.add(i)
        }
        assertEquals(listOf(1, 3, 5, 7, 9), stepResult)
    }
    
    // Type casting and checking
    @Test
    fun typeCasting_safeOperations() {
        val obj: Any = "Hello"
        val str = obj as? String
        val num = obj as? Int
        
        assertEquals("Hello", str)
        assertNull(num)
        
        assertTrue(obj is String)
        assertFalse(obj is Int)
        assertTrue(42 is Number)
    }
    
    @Test(expected = ClassCastException::class)
    fun typeCasting_unsafeOperation_shouldThrowException() {
        val obj: Any = "Hello"
        @Suppress("UNUSED_VARIABLE")
        val num = obj as Int
    }
    
    // Extension functions for custom behavior
    @Test
    fun extensionFunction_stringExtensions() {
        fun String.isPalindrome(): Boolean {
            val cleaned = this.lowercase().replace(Regex("[^a-z]"), "")
            return cleaned == cleaned.reversed()
        }
        
        fun String.wordCount(): Int = this.split("\\s+".toRegex()).size
        
        assertTrue("racecar".isPalindrome())
        assertTrue("A man a plan a canal Panama".isPalindrome())
        assertFalse("hello".isPalindrome())
        
        assertEquals(3, "Hello World Test".wordCount())
        assertEquals(1, "Hello".wordCount())
    }
    
    @Test
    fun extensionFunction_numberExtensions() {
        fun Int.isEven(): Boolean = this % 2 == 0
        fun Int.square(): Int = this * this
        fun Int.factorial(): Long = if (this <= 1) 1 else this * (this - 1).factorial()
        
        assertTrue(4.isEven())
        assertFalse(3.isEven())
        assertEquals(25, 5.square())
        assertEquals(0, 0.square())
        assertEquals(120L, 5.factorial())
    }
    
    // Data class tests for model objects
    @Test
    fun dataClass_basicFunctionality() {
        val user1 = UserProfile(1, "Alice", "alice@test.com")
        val user2 = UserProfile(1, "Alice", "alice@test.com")
        val user3 = UserProfile(2, "Bob", "bob@test.com")
        
        assertEquals(user1, user2)
        assertNotEquals(user1, user3)
        assertEquals(user1.hashCode(), user2.hashCode())
        
        val modifiedUser = user1.copy(name = "Alice Johnson")
        assertEquals("Alice Johnson", modifiedUser.name)
        assertEquals("alice@test.com", modifiedUser.email)
        assertEquals("Alice", user1.name) // Original unchanged
    }
    
    @Test
    fun dataClass_businessLogic() {
        val validUser = UserProfile(1, "Alice", "alice@example.com")
        val anonymousUser = UserProfile(2, "", "user@test.com")
        val invalidEmailUser = UserProfile(3, "Bob", "invalid-email")
        
        assertEquals("Alice", validUser.getDisplayName())
        assertEquals("Anonymous User", anonymousUser.getDisplayName())
        
        assertTrue(validUser.isValidEmail())
        assertTrue(anonymousUser.isValidEmail())
        assertFalse(invalidEmailUser.isValidEmail())
    }
    
    @Test
    fun dataClass_destructuring() {
        val user = UserProfile(1, "Alice", "alice@test.com")
        val (id, name, email, isActive) = user
        
        assertEquals(1, id)
        assertEquals("Alice", name)
        assertEquals("alice@test.com", email)
        assertTrue(isActive)
    }
    
    // Enum tests for state management
    @Test
    fun enum_basicOperations() {
        val states = NetworkState.values()
        assertEquals(4, states.size)
        assertEquals(NetworkState.IDLE, states[0])
        assertEquals(NetworkState.LOADING, states[1])
        assertEquals(NetworkState.SUCCESS, states[2])
        assertEquals(NetworkState.ERROR, states[3])
        
        assertEquals(NetworkState.IDLE, NetworkState.valueOf("IDLE"))
        assertEquals(NetworkState.SUCCESS, NetworkState.valueOf("SUCCESS"))
        
        assertEquals(0, NetworkState.IDLE.ordinal)
        assertEquals(2, NetworkState.SUCCESS.ordinal)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun enum_valueOf_invalidName_shouldThrowException() {
        NetworkState.valueOf("INVALID_STATE")
    }
    
    // Sealed class tests for type-safe state handling
    @Test
    fun sealedClass_patternMatching() {
        fun handleApiResult(result: ApiResult<String>): String = when (result) {
            is ApiResult.Success -> "Success: ${result.data}"
            is ApiResult.Error -> "Error ${result.code}: ${result.message}"
            is ApiResult.Loading -> "Loading..."
        }
        
        assertEquals("Success: Hello", handleApiResult(ApiResult.Success("Hello")))
        assertEquals("Error 404: Not Found", handleApiResult(ApiResult.Error("Not Found", 404)))
        assertEquals("Error -1: Network Error", handleApiResult(ApiResult.Error("Network Error")))
        assertEquals("Loading...", handleApiResult(ApiResult.Loading))
    }
    
    // Higher-order functions and lambda expressions
    @Test
    fun higherOrderFunction_standardLibrary() {
        val numbers = listOf(1, 2, 3, 4, 5, 6)
        
        val evenNumbers = numbers.filter { it % 2 == 0 }
        assertEquals(listOf(2, 4, 6), evenNumbers)
        
        val squared = numbers.map { it * it }
        assertEquals(listOf(1, 4, 9, 16, 25, 36), squared)
        
        val sum = numbers.reduce { acc, n -> acc + n }
        assertEquals(21, sum)
        
        val firstEven = numbers.find { it % 2 == 0 }
        assertEquals(2, firstEven)
    }
    
    @Test
    fun higherOrderFunction_customImplementation() {
        fun <T> List<T>.customFilter(predicate: (T) -> Boolean): List<T> {
            val result = mutableListOf<T>()
            for (item in this) {
                if (predicate(item)) {
                    result.add(item)
                }
            }
            return result
        }
        
        fun <T, R> List<T>.customMap(transform: (T) -> R): List<R> {
            val result = mutableListOf<R>()
            for (item in this) {
                result.add(transform(item))
            }
            return result
        }
        
        val numbers = listOf(1, 2, 3, 4, 5)
        val greaterThanTwo = numbers.customFilter { it > 2 }
        assertEquals(listOf(3, 4, 5), greaterThanTwo)
        
        val doubled = numbers.customMap { it * 2 }
        assertEquals(listOf(2, 4, 6, 8, 10), doubled)
    }
    
    @Test
    fun lambdaExpressions_variousFormats() {
        val add: (Int, Int) -> Int = { a, b -> a + b }
        val multiply = { a: Int, b: Int -> a * b }
        val greet: (String) -> String = { name -> "Hello, $name!" }
        
        assertEquals(5, add(2, 3))
        assertEquals(6, multiply(2, 3))
        assertEquals("Hello, Alice!", greet("Alice"))
    }
    
    // String template expressions for UI text generation
    @Test
    fun stringTemplate_basicInterpolation() {
        val name = "Alice"
        val age = 30
        val score = 95.5
        
        assertEquals("Hello, Alice!", "Hello, $name!")
        assertEquals("Alice is 30 years old", "$name is $age years old")
        assertEquals("Score: 95.5%", "Score: $score%")
    }
    
    @Test
    fun stringTemplate_expressionEvaluation() {
        val a = 5
        val b = 3
        val list = listOf("apple", "banana", "cherry")
        
        assertEquals("5 + 3 = 8", "$a + $b = ${a + b}")
        assertEquals("The first item is apple", "The first item is ${list.first()}")
        assertEquals("List has 3 items", "List has ${list.size} items")
        assertEquals("Uppercase: HELLO", "Uppercase: ${"hello".uppercase()}")
    }
    
    // Collection processing patterns for data handling
    @Test
    fun collectionProcessing_userProfiles() {
        val activeUsers = userProfiles.filter { it.isActive }
        assertEquals(1, activeUsers.size)
        assertEquals("Alice Johnson", activeUsers.first().name)
        
        val userNames = userProfiles.map { it.getDisplayName() }
        assertEquals(listOf("Alice Johnson", "Bob Smith", "Anonymous User"), userNames)
        
        val validEmails = userProfiles.count { it.isValidEmail() }
        assertEquals(2, validEmails)
        
        val hasActiveUser = userProfiles.any { it.isActive }
        assertTrue(hasActiveUser)
        
        val allHaveIds = userProfiles.all { it.id > 0 }
        assertTrue(allHaveIds)
    }
    
    @Test
    fun collectionProcessing_groupingAndSorting() {
        val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        
        val groupedByEvenOdd = numbers.groupBy { if (it % 2 == 0) "even" else "odd" }
        assertEquals(5, groupedByEvenOdd["odd"]?.size)
        assertEquals(5, groupedByEvenOdd["even"]?.size)
        
        val sorted = listOf(3, 1, 4, 1, 5, 9, 2, 6).sorted()
        assertEquals(listOf(1, 1, 2, 3, 4, 5, 6, 9), sorted)
        
        val usersSortedByName = userProfiles.sortedBy { it.name }
        assertEquals("", usersSortedByName.first().name) // Empty name comes first
        assertEquals("Bob Smith", usersSortedByName.last().name)
    }
    
    // Error handling patterns
    @Test
    fun errorHandling_trycatchPatterns() {
        fun parseIntSafely(str: String): Int? {
            return try {
                str.toInt()
            } catch (e: NumberFormatException) {
                null
            }
        }
        
        assertEquals(42, parseIntSafely("42"))
        assertNull(parseIntSafely("invalid"))
        assertNull(parseIntSafely(""))
    }
    
    @Test
    fun errorHandling_resultPatterns() {
        fun divideNumbers(a: Int, b: Int): ApiResult<Double> {
            return if (b == 0) {
                ApiResult.Error("Division by zero", 400)
            } else {
                ApiResult.Success(a.toDouble() / b)
            }
        }
        
        val successResult = divideNumbers(10, 2)
        assertTrue(successResult is ApiResult.Success)
        assertEquals(5.0, (successResult as ApiResult.Success).data, 0.001)
        
        val errorResult = divideNumbers(10, 0)
        assertTrue(errorResult is ApiResult.Error)
        assertEquals("Division by zero", (errorResult as ApiResult.Error).message)
    }
}