//package br.com.vinisantosdev.satsflow.web
//
//import br.com.vinisantosdev.satsflow.usecase.EscrowUseCase
//import org.springframework.http.HttpStatus
//import org.springframework.security.access.prepost.PreAuthorize
//import org.springframework.web.bind.annotation.*
//import reactor.core.publisher.Mono
//import java.util.*
//
//
//@RestController
//@RequestMapping("/api/v1/admin/escrow")
//class EscrowAdminController(
//    private val escrowUseCase: EscrowUseCase,
//) {
//
//    @PostMapping("/{id}/resolve")
//    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasRole('ADMIN')")
//    fun resolveDispute(
//        @PathVariable id: UUID,
//        @RequestBody request: ResolveDisputeRequest,
//    ): Mono<Void> =
//        escrowUseCase.resolveDispute(id, request.resolution, request.reason)
//}