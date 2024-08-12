package ru.itcollege.userservice.routes.authorization.grpc

import io.grpc.stub.StreamObserver
import org.lognet.springboot.grpc.GRpcService
import ru.itcollege.grpc.authorization.AuthorizationServiceGrpc.AuthorizationServiceImplBase
import ru.itcollege.grpc.authorization.JWTPayload
import ru.itcollege.grpc.authorization.User
import ru.itcollege.userservice.core.domain.providers.JwtProvider

@GRpcService
class AuthorizationService(private var jwtProvider: JwtProvider) : AuthorizationServiceImplBase() {

  /**
   * ## getUserByAccess
   *
   * Метод возвращает имя пользователя по токену авторизации.
   *
   * @param request
   * @param responseObserver
   * */

  override fun getUserByAccess(request: JWTPayload?, responseObserver: StreamObserver<User>?) {
    val current = User.newBuilder().setUid(request?.access?.let { this.jwtProvider.read(it) }).build()
    responseObserver?.onNext(current)
    responseObserver?.onCompleted()
  }
}