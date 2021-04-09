package com.horrors.newhorror


class Singleton {
    var givenName: String? = null
    var displayName: String?  = null
    var mail: String? = null
    var supclass1: Boolean = false
    var position1 = 0
    var name: String? = null


    companion object {
        private var INSTANCE: Singleton? = null

        val instance: Singleton
            get() {
                if (INSTANCE == null) {
                    INSTANCE = Singleton()
                }
                return INSTANCE!!
            }
    }
}