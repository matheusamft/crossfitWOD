package com.ifsp.crossfitwod.data.repository

import com.ifsp.crossfitwod.data.datastore.WodDataStore
import com.ifsp.crossfitwod.data.model.Exercicio
import com.ifsp.crossfitwod.data.model.Resultado
import com.ifsp.crossfitwod.data.model.Wod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.UUID

class WodRepository(private val wodDataStore: WodDataStore) {

    fun getWods(): Flow<List<Wod>> = wodDataStore.wodsFlow

    suspend fun addWod(wod: Wod) {
        val currentWods = wodDataStore.wodsFlow.first().toMutableList()
        currentWods.add(wod)
        wodDataStore.saveWods(currentWods)
    }

    suspend fun updateWod(updatedWod: Wod) {
        val currentWods = wodDataStore.wodsFlow.first().map {
            if (it.id == updatedWod.id) updatedWod else it
        }
        wodDataStore.saveWods(currentWods)
    }

    suspend fun deleteWod(wodId: String) {
        val currentWods = wodDataStore.wodsFlow.first().filter { it.id != wodId }
        wodDataStore.saveWods(currentWods)
    }

    suspend fun addResult(wodId: String, resultado: Resultado) {
        val currentWods = wodDataStore.wodsFlow.first().map { wod ->
            if (wod.id == wodId) {
                wod.copy(resultados = wod.resultados + resultado)
            } else {
                wod
            }
        }
        wodDataStore.saveWods(currentWods)
    }

    suspend fun seedInitialDataIfEmpty() {
        val currentWods = wodDataStore.wodsFlow.first()
        if (currentWods.isEmpty()) {
            val initialWods = listOf(
                Wod(
                    id = UUID.randomUUID().toString(),
                    nome = "Fran",
                    tipo = "For Time",
                    descricao = "21-15-9 Thrusters e Pull-ups",
                    exercicios = listOf(
                        Exercicio("Thrusters", "21-15-9", 43.0),
                        Exercicio("Pull-ups", "21-15-9")
                    )
                ),
                Wod(
                    id = UUID.randomUUID().toString(),
                    nome = "Cindy",
                    tipo = "AMRAP",
                    tempoMinutos = 20,
                    descricao = "5 Pull-ups, 10 Push-ups e 15 Air Squats",
                    exercicios = listOf(
                        Exercicio("Pull-ups", "5"),
                        Exercicio("Push-ups", "10"),
                        Exercicio("Air Squats", "15")
                    )
                ),
                Wod(
                    id = UUID.randomUUID().toString(),
                    nome = "Grace",
                    tipo = "For Time",
                    descricao = "30 Clean & Jerks",
                    exercicios = listOf(
                        Exercicio("Clean & Jerks", "30", 61.0)
                    )
                )
            )
            wodDataStore.saveWods(initialWods)
        }
    }
}
