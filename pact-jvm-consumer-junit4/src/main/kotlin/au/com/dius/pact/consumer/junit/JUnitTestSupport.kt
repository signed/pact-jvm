package au.com.dius.pact.consumer.junit

import au.com.dius.pact.consumer.Pact
import au.com.dius.pact.consumer.PactMismatchesException
import au.com.dius.pact.consumer.PactVerificationResult
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.model.RequestResponsePact

import java.lang.reflect.Method

object JUnitTestSupport {
  /**
   * validates method signature as described at [Pact]
   */
  @JvmStatic
  fun conformsToSignature(m: Method): Boolean {
    val pact = m.getAnnotation(Pact::class.java)
    val conforms = (pact != null &&
      RequestResponsePact::class.java.isAssignableFrom(m.returnType) &&
      m.parameterTypes.size == 1 &&
      m.parameterTypes[0].isAssignableFrom(PactDslWithProvider::class.java))

    if (!conforms && pact != null) {
      throw UnsupportedOperationException("Method ${m.name} does not conform required method signature " +
        "'public RequestResponsePact xxx(PactDslWithProvider builder)'")
    }

    return conforms
  }

  @JvmStatic
  fun validateMockServerResult(result: PactVerificationResult) {
    if (result != PactVerificationResult.Ok) {
      if (result is PactVerificationResult.Error) {
        if (result.mockServerState !== PactVerificationResult.Ok) {
          throw AssertionError("Pact Test function failed with an exception, possibly due to " + result.mockServerState, result.error)
        } else {
          throw AssertionError("Pact Test function failed with an exception: " + result.error.message, result.error)
        }
      } else {
        throw PactMismatchesException(result)
      }
    }
  }
}
