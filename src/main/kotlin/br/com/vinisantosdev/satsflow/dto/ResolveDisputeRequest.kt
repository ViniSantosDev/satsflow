package br.com.vinisantosdev.satsflow.dto

import br.com.vinisantosdev.satsflow.enuns.EscrowStatus

data class ResolveDisputeRequest(
    val resolution: EscrowStatus, // RELEASED ou REFUNDED
    val reason: String,
)
