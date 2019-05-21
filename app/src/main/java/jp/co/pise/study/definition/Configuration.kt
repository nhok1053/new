package jp.co.pise.study.definition

class ApiConfig {
    companion object {
        @JvmField val CONNECTION_TIMEOUT_SEC: Long = 30
        @JvmField val READ_TIMEOUT_SEC: Long = 30
    }
}

class DbConfig {
    companion object {
        @JvmField val DB_NAME = "TemplateDb"
        @JvmField val DB_VERSION_1 = 1
        @JvmField val DB_VERSION_NEWEST = DB_VERSION_1
    }
}