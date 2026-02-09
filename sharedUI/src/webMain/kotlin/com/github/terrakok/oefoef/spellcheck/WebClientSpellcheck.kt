package com.github.terrakok.oefoef.spellcheck

import oefoef.sharedui.generated.resources.Res
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.toArray

private suspend fun readDictionary(name: String): String {
    return Res.readBytes("files/dict/$name").decodeToString()
}

private suspend fun createNSpell(): NSpell =
    nspell(readDictionary("nl.aff"), readDictionary("nl.dic"))


class WebClientSpellcheck : ClientSpellcheck {

    override val enabled: Boolean = true

    private var nSpell: NSpell? = null

    private suspend fun init() {
        if (nSpell == null) {
            nSpell = createNSpell()
        }
    }

    override suspend fun correct(word: String): Boolean {
        init()
        return nSpell!!.correct(word)
    }

    @OptIn(ExperimentalWasmJsInterop::class)
    override suspend fun suggest(word: String): List<String> {
        init()
        return nSpell!!.suggest(word).toArray().map { it.toString() }
    }
}