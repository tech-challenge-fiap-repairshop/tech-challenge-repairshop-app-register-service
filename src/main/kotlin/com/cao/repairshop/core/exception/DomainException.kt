package com.cao.repairshop.core.exception

open class DomainException(message: String) : RuntimeException(message)

class EntityNotFoundException(message: String) : DomainException(message)
class DuplicateEntityException(message: String) : DomainException(message)
class BusinessRuleViolationException(message: String) : DomainException(message)
class InvalidStateTransitionException(message: String) : DomainException(message)
class InsufficientStockException(message: String) : DomainException(message)
class InvalidDocumentException(message: String) : DomainException(message)
class InvalidPlateException(message: String) : DomainException(message)
class PasswordEncodingException(message: String) : DomainException(message)
