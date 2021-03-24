package top.colter.mirai.plugin

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "top.colter.bilibili-dynamic-mirai-plugin",
        name = "BilibiliDynamic",
        version = "1.0.0"
    ) {
        author("Colter")

        info("""
            把B站订阅者的动态转发到QQ
        """.trimIndent())
    }
) {
    // 动态历史记录
    val historyDynamic : MutableList<String> = mutableListOf()

    // b站表情
    val emojiMap = mutableMapOf<String,java.awt.Image>()

    var ys = mutableMapOf<String,Int>()

    lateinit var bot : Bot

    override fun onEnable() {
        logger.info { "Plugin loaded" }

        //加载插件配置数据
        PluginConfig.reload()
        //加载插件数据
        PluginData.reload()

        MySimpleCommand.register()

        //设置运行路径
        PluginData.runPath = System.getProperty("user.dir")

        PluginMain.launch {
            logger.info("forward......")

            init()
            delay(2000)

            Bot.instances.forEach { b: Bot ->
                bot = b
            }
            bot.eventChannel.registerListenerHost(NewFriendRequestListener)
            bot.eventChannel.registerListenerHost(MemberJoinListener)
            bot.eventChannel.registerListenerHost(MessageListener)

            check(bot)

        }
    }

    override fun onDisable() {
        MySimpleCommand.unregister()
    }
}