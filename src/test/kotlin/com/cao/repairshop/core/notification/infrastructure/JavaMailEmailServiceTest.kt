package com.cao.repairshop.core.notification.infrastructure

import com.cao.repairshop.core.notification.dto.EmailRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.mail.internet.MimeMessage
import org.junit.jupiter.api.Test
import org.springframework.mail.javamail.JavaMailSender

class JavaMailEmailServiceTest {

    private val mailSender = mockk<JavaMailSender>()
    private val emailService = JavaMailEmailService(mailSender)

    @Test
    fun `should send email successfully`() {
        // Given
        val request = EmailRequest(
            to = "test@example.com",
            subject = "Test Subject",
            body = "Test Body"
        )
        val mimeMessage = mockk<MimeMessage>(relaxed = true)

        every { mailSender.createMimeMessage() } returns mimeMessage
        every { mailSender.send(any<MimeMessage>()) } returns Unit

        // When
        emailService.sendEmail(request)

        // Then
        verify { mailSender.createMimeMessage() }
        verify { mailSender.send(mimeMessage) }
    }
}
