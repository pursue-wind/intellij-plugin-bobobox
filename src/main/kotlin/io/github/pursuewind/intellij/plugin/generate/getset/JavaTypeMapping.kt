package io.github.pursuewind.intellij.plugin.generate.getset

object JavaTypeMapping {

    val baseTypeMapping: Map<String, String> by lazy {
        mapOf(
            "byte" to "(byte) 0",
            "short" to "(short) 0",
            "int" to "0",
            "integer" to "0",
            "char" to "0",
            "character" to "'0'",
            "boolean" to "false",
            "double" to "0D",
            "float" to "0f",
            "long" to "0L",
            "string" to "\"\""
        )
    }

    val mapping: Map<String, String> by lazy {
        mapOf(
            "java.util.List" to "new java.util.ArrayList<>()",
            "java.util.Map" to "new java.util.HashMap<>(2)",
            "java.util.Set" to "new java.util.HashSet<>()",
            "java.util.Collection" to "new java.util.ArrayList<>()",
            "org.springframework.web.multipart.MultipartFile" to "null",
            "java.lang.Number" to "0",
            "java.math.BigDecimal" to "new java.math.BigDecimal(0)",
            "java.util.Date" to "new java.util.Date()",
            "java.sql.Date" to "new java.sql.Date(System.currentTimeMillis())",
            "java.sql.Timestamp" to "new java.sql.Timestamp(System.currentTimeMillis())",
            "java.sql.Time" to "new java.sql.Time(System.currentTimeMillis())",
            "java.sql.Blob" to "new java.sql.SerialBlob(new byte[]{})",
            "java.sql.Clob" to "new java.sql.SerialClob(new char[]{})",
        )
    }


}
