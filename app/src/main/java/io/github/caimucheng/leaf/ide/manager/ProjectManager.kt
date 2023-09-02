package io.github.caimucheng.leaf.ide.manager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ProjectManager {

    suspend fun fetchProjects() {
        return withContext(Dispatchers.IO) {

        }
    }

}