package au.com.dius.pact.provider.junit

import au.com.dius.pact.model.FilteredPact
import au.com.dius.pact.model.Interaction
import au.com.dius.pact.model.Pact
import au.com.dius.pact.model.v3.messaging.MessagePact

/**
 * Pact runner that only verifies message pacts
 */
open class MessagePactRunner<I>(clazz: Class<*>) : PactRunner<I>(clazz) where I: Interaction {
  override fun filterPacts(pacts: List<Pact<I>>): List<Pact<I>> {
    return super.filterPacts(pacts).filter { pact ->
      pact is MessagePact || (pact is FilteredPact && pact.pact is MessagePact)
    }
  }
}
