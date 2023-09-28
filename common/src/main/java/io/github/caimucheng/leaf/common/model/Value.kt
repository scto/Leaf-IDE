package io.github.caimucheng.leaf.common.model

class Value<T>(var value: T) {

    fun copy(value: T = this.value): Value<T> {
        return Value(value)
    }

}