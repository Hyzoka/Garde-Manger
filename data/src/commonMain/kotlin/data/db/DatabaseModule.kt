package data.db

import app.cash.sqldelight.db.SqlDriver

fun createDatabase(driver: SqlDriver): AppDatabase {
    return AppDatabase(driver)
}