package io.github.pursuewind.intellij.plugin.inline.ui.refactor

import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.util.messages.Topic
import com.intellij.util.xmlb.XmlSerializerUtil
import io.github.pursuewind.intellij.plugin.inline.data.DefaultSettings

/**
 * Settings
 */
@State(name = "ShitBox.Settings", storages = [(Storage("io.github.pursuewind.intellij.plugin.Settings.xml"))])
class Settings : PersistentStateComponent<Settings> {
    var numberOfWhitespaces = DefaultSettings.NUMBER_OF_WHITESPACES
    var effectType = DefaultSettings.EFFECT_TYPE
    var maxErrorsPerLine = DefaultSettings.MAX_ERRORS_PER_LINE
    var font = DefaultSettings.FONT
    var textStyle = DefaultSettings.TEXT_STYLE
    var oneGutterMode = DefaultSettings.ONE_GUTTER_MODE
    var error = DefaultSettings.ERROR
    var warning = DefaultSettings.WARNING
    var weakWarning = DefaultSettings.WEAK_WARNING
    var information = DefaultSettings.INFORMATION
    var serverError = DefaultSettings.SERVER_ERROR
    var otherError = DefaultSettings.OTHER_ERROR
    var ignoreList = DefaultSettings.IGNORE_LIST


    override fun getState(): Settings = this

    override fun loadState(state: Settings) {
        XmlSerializerUtil.copyBean(state, this)

        val properties: PropertiesComponent = PropertiesComponent.getInstance()
        val dataVersion = properties.getInt(DATA_VERSION_KEY, 0)

        thisLogger().debug("===== Settings Data Version: $dataVersion =====")
        if (dataVersion < CURRENT_DATA_VERSION) {
            migrate()
            properties.setValue(DATA_VERSION_KEY, CURRENT_DATA_VERSION, 0)
        }
    }

    companion object {

        /**
         * Get the instance of this service.
         *
         * @return the unique [Settings] instance.
         */
        val instance: Settings
            get() = ApplicationManager.getApplication().getService(Settings::class.java)


        private const val CURRENT_DATA_VERSION = 1
        private const val DATA_VERSION_KEY = "settings.data.version"



        //region Data Migration - Will be removed on v4.0
        private fun Settings.migrate() {
            thisLogger().debug("===== Start Migration =====")
            with(PasswordSafe.instance) {
            }
            thisLogger().debug("===== Migration End =====")
        }

        //endregion
    }
}

private const val YOUDAO_SERVICE_NAME = "YIIGUXING.TRANSLATION"
private const val YOUDAO_APP_KEY = "YOUDAO_APP_KEY"
private const val BAIDU_SERVICE_NAME = "YIIGUXING.TRANSLATION.BAIDU"
private const val BAIDU_APP_KEY = "BAIDU_APP_KEY"
private const val ALI_SERVICE_NAME = "YIIGUXING.TRANSLATION.ALI"
private const val ALI_APP_KEY = "ALI_APP_KEY"


private val SETTINGS_CHANGE_PUBLISHER: SettingsChangeListener =
    ApplicationManager.getApplication().messageBus.syncPublisher(SettingsChangeListener.TOPIC)




interface SettingsChangeListener {


    fun onTranslatorConfigurationChanged() {}

    fun onOverrideFontChanged(settings: Settings) {}

    fun onWordbookStoragePathChanged(settings: Settings) {}

    companion object {
        val TOPIC: Topic<SettingsChangeListener> =
            Topic.create("TranslationSettingsChanged", SettingsChangeListener::class.java)
    }
}
