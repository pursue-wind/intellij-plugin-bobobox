package io.github.pursuewind.intellij.plugin.generate.trans

import io.github.pursuewind.intellij.plugin.generate.util.md5

class TransApi(private val appid: String, private val securityKey: String) {
    companion object {
        private const val TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/fieldtranslate"
    }

    fun buildUrl(query: String): String {
        val queryString = buildParams(query).entries.joinToString("&") { (key, value) ->
            "$key=$value"
        }

        return "$TRANS_API_HOST?$queryString"
    }

    private fun buildParams(
        query: String,
        from: String = "auto",
        to: String = "en",
        domain: String = "it"
    ): Map<String, String> {
        val salt = System.currentTimeMillis().toString()
        return mapOf(
            "q" to query,
            "from" to from,
            "to" to to,
            "domain" to domain,
            "appid" to appid,
            "salt" to salt,
            "sign" to ("$appid$query$salt$domain$securityKey").md5()
        )
    }
}

