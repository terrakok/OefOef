@file:OptIn(ExperimentalWasmJsInterop::class)
@file:JsModule("nspell")

package com.github.terrakok.oefoef.data.spellcheck

import kotlin.js.*

@JsName("default")
external fun nspell(
    aff: String,
    dic: String,
): NSpell

external interface NSpell {
    fun correct(word: String): Boolean

    fun suggest(word: String): JsArray<JsString>

    fun add(word: String): NSpell

    fun remove(word: String): NSpell
}
