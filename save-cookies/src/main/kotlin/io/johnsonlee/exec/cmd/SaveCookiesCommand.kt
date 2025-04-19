package io.johnsonlee.exec.cmd

import com.google.auto.service.AutoService
import com.microsoft.playwright.Page
import com.microsoft.playwright.options.Cookie
import io.johnsonlee.exec.internal.playwright.PlaywrightManager
import io.johnsonlee.exec.internal.playwright.asHttpCookie
import io.johnsonlee.exec.internal.playwright.waitForClose
import java.io.File
import kotlin.system.exitProcess
import picocli.CommandLine

@AutoService(Command::class)
class SaveCookiesCommand : IOCommand() {

    override fun run() = PlaywrightManager.newContext().use { context ->
        input.map { url ->
            context.newPage().apply {
                navigate(url)
            }
        }.onEach(Page::waitForClose)

        val cookies = context.cookies().map(Cookie::asHttpCookie)
        File(output).writeText(cookies.joinToString(",", prefix = "[", postfix = "]") {
            """
            {
                "name": "${it.name}",
                "value": "${it.value}",
                "domain": "${it.domain}",
                "path": "${it.path}",
                "maxAge": ${it.maxAge},
                "httpOnly": ${it.isHttpOnly},
                "secure": ${it.secure}
            }
            """.trimIndent()
        })
    }

}

fun main(args: Array<String>) {
    exitProcess(CommandLine(SaveCookiesCommand()).execute(*args))
}