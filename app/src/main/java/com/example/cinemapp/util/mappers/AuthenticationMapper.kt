package com.example.cinemapp.util.mappers

import com.example.cinemapp.data.model.SessionResponseDTO
import com.example.cinemapp.ui.main.model.SessionResponse

class AuthenticationMapper {
    fun mapToSessionResponse(sessionResponseDTO: SessionResponseDTO): SessionResponse {
        return SessionResponse(
            sessionResponseDTO.success ?: false,
            sessionResponseDTO.sessionId ?: ""
        )
    }
}