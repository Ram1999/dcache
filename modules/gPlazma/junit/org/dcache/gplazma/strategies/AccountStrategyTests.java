package org.dcache.gplazma.strategies;

import java.security.Principal;
import java.util.Set;
import java.util.HashSet;
import org.dcache.gplazma.AuthenticationException;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.dcache.gplazma.SessionID;
import org.dcache.gplazma.plugins.GPlazmaAccountPlugin;
import static org.dcache.gplazma.configuration.ConfigurationItemControl.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableList;

/**
 *
 * @author timur
 */
public class AccountStrategyTests {

    private static final String DefaultStrategyFactory =
            "org.dcache.gplazma.strategies.DefaultStrategyFactory";
    private StrategyFactory strategyFactory;

    private List<GPlazmaPluginElement<GPlazmaAccountPlugin>> emptyList =
            Lists.newArrayList();

    private List<GPlazmaPluginElement<GPlazmaAccountPlugin>> oneDoNothingPlugins =
        ImmutableList.of(new GPlazmaPluginElement<GPlazmaAccountPlugin>(new DoNothingStrategy(),REQUIRED));

    private List<GPlazmaPluginElement<GPlazmaAccountPlugin>> successRequiredPlugins =
        ImmutableList.of(new GPlazmaPluginElement<GPlazmaAccountPlugin>(new DoNothingStrategy(),REQUIRED),
                         new GPlazmaPluginElement<GPlazmaAccountPlugin>(new AlwaysAccountStrategy(),REQUIRED));

    private List<GPlazmaPluginElement<GPlazmaAccountPlugin>> successOptionalPlugins =
        ImmutableList.of(new GPlazmaPluginElement<GPlazmaAccountPlugin>(new DoNothingStrategy(),OPTIONAL),
                         new GPlazmaPluginElement<GPlazmaAccountPlugin>(new AlwaysAccountStrategy(),OPTIONAL));

    private List<GPlazmaPluginElement<GPlazmaAccountPlugin>> successRequisitePlugins =
        ImmutableList.of(new GPlazmaPluginElement<GPlazmaAccountPlugin>(new DoNothingStrategy(),REQUISITE),
                         new GPlazmaPluginElement<GPlazmaAccountPlugin>(new AlwaysAccountStrategy(),REQUISITE));

    private List<GPlazmaPluginElement<GPlazmaAccountPlugin>> successSufficientPlugins =
        ImmutableList.of(new GPlazmaPluginElement<GPlazmaAccountPlugin>(new DoNothingStrategy(),SUFFICIENT),
                         new GPlazmaPluginElement<GPlazmaAccountPlugin>(new AlwaysAccountStrategy(),SUFFICIENT));

    private List<GPlazmaPluginElement<GPlazmaAccountPlugin>> failedPlugins =
        ImmutableList.of(new GPlazmaPluginElement<GPlazmaAccountPlugin>(new AlwaysAccountStrategy(),REQUIRED),
                         new GPlazmaPluginElement<GPlazmaAccountPlugin>(new ThrowAuthenticationExceptionStrategy(),REQUIRED));

    private List<GPlazmaPluginElement<GPlazmaAccountPlugin>> testOptionalFailingPlugins =
        ImmutableList.of(new GPlazmaPluginElement<GPlazmaAccountPlugin>(new AlwaysAccountStrategy(),REQUIRED),
                         new GPlazmaPluginElement<GPlazmaAccountPlugin>(new ThrowAuthenticationExceptionStrategy(),OPTIONAL));

    private List<GPlazmaPluginElement<GPlazmaAccountPlugin>> testRequesitePlugins1 =
        ImmutableList.of(new GPlazmaPluginElement<GPlazmaAccountPlugin>(new ThrowTestAuthenticationExceptionStrategy(),REQUISITE),
                         new GPlazmaPluginElement<GPlazmaAccountPlugin>(new ThrowRuntimeExceptionStrategy(),REQUIRED));

    private List<GPlazmaPluginElement<GPlazmaAccountPlugin>> testRequesitePlugins2 =
        ImmutableList.of(new GPlazmaPluginElement<GPlazmaAccountPlugin>(new ThrowTestAuthenticationExceptionStrategy(),REQUIRED),
                         new GPlazmaPluginElement<GPlazmaAccountPlugin>(new ThrowAuthenticationExceptionStrategy(),REQUISITE),
                         new GPlazmaPluginElement<GPlazmaAccountPlugin>(new ThrowRuntimeExceptionStrategy(),REQUIRED));

    private List<GPlazmaPluginElement<GPlazmaAccountPlugin>> sufficientPluginFollowedByFailedArray =
        ImmutableList.of(new GPlazmaPluginElement<GPlazmaAccountPlugin>(new AlwaysAccountStrategy(),SUFFICIENT),
                         new GPlazmaPluginElement<GPlazmaAccountPlugin>(new ThrowRuntimeExceptionStrategy(),REQUIRED));

    @Before
    public void setUp() {
        strategyFactory = StrategyFactory.getInstance(DefaultStrategyFactory);
    }


    @Test
    public void testDefaultFactoryGetInstanceReturnsAFactory() {
        StrategyFactory factory =
                StrategyFactory.getInstance();
        assertNotNull(factory);
        AccountStrategy authStrategy = factory.newAccountStrategy();
        assertNotNull(authStrategy);
    }

    /**
     *
     * @throws org.dcache.gplazma.AuthenticationException
     */
    @Test
    public void testEmptyConfig() throws AuthenticationException{

        AccountStrategy strategy =
                strategyFactory.newAccountStrategy();
        assertNotNull(strategy);
        strategy.setPlugins(emptyList);
        TestSessionId sessionId = new TestSessionId();
        sessionId.setSessionID(Integer.valueOf(0));
        Set<Principal> authorizedPrincipals = Sets.newHashSet();
        strategy.account(sessionId,
                authorizedPrincipals);
    }

    @Test
    public void testDoNothingOneElementConfig() throws AuthenticationException{

        AccountStrategy strategy =
                strategyFactory.newAccountStrategy();
        assertNotNull(strategy);
        strategy.setPlugins(oneDoNothingPlugins);
        TestSessionId sessionId = new TestSessionId();
        sessionId.setSessionID(Integer.valueOf(0));
        Set<Principal> authorizedPrincipals = Sets.newHashSet();
        strategy.account(sessionId,
                authorizedPrincipals);
    }

    @Test (expected=AuthenticationException.class)
    public void testFailedConfig() throws AuthenticationException{

        AccountStrategy strategy =
                strategyFactory.newAccountStrategy();
        assertNotNull(strategy);
        strategy.setPlugins(failedPlugins);
        TestSessionId sessionId = new TestSessionId();
        sessionId.setSessionID(Integer.valueOf(0));
        Set<Principal> authorizedPrincipals = Sets.newHashSet();
        strategy.account(sessionId,
                authorizedPrincipals);
    }

    @Test
    public void testRequiredConfig() throws AuthenticationException{

        AccountStrategy strategy =
                strategyFactory.newAccountStrategy();
        assertNotNull(strategy);
        strategy.setPlugins(successRequiredPlugins);
        TestSessionId sessionId = new TestSessionId();
        sessionId.setSessionID(Integer.valueOf(0));
        Set<Principal> authorizedPrincipals = Sets.newHashSet();
        strategy.account(sessionId,
                authorizedPrincipals);
    }

    @Test
    public void testRequisiteConfig() throws AuthenticationException{

        AccountStrategy strategy =
                strategyFactory.newAccountStrategy();
        assertNotNull(strategy);
        strategy.setPlugins(successRequisitePlugins);
        TestSessionId sessionId = new TestSessionId();
        sessionId.setSessionID(Integer.valueOf(0));
        Set<Principal> authorizedPrincipals = Sets.newHashSet();
        strategy.account(sessionId,
                authorizedPrincipals);
    }
    @Test
    public void testOptionalConfig() throws AuthenticationException{

        AccountStrategy strategy =
                strategyFactory.newAccountStrategy();
        assertNotNull(strategy);
        strategy.setPlugins(successOptionalPlugins);
        TestSessionId sessionId = new TestSessionId();
        sessionId.setSessionID(Integer.valueOf(0));
        Set<Principal> authorizedPrincipals = Sets.newHashSet();
        strategy.account(sessionId,
                authorizedPrincipals);
    }

    @Test
    public void testSufficientConfig() throws AuthenticationException{

        AccountStrategy strategy =
                strategyFactory.newAccountStrategy();
        assertNotNull(strategy);
        strategy.setPlugins(successSufficientPlugins);
        TestSessionId sessionId = new TestSessionId();
        sessionId.setSessionID(Integer.valueOf(0));
        Set<Principal> authorizedPrincipals = Sets.newHashSet();
        strategy.account(sessionId,
                authorizedPrincipals);
    }

    /**
     * in this case the first sufficient plugin should succeed and the second plugin
     * that throws RuntimeException should be never called
     */
    @Test
    public void testSufficientPluginFollowedByFailedConfig() throws AuthenticationException{
        AccountStrategy strategy =
                strategyFactory.newAccountStrategy();
        assertNotNull(strategy);
        strategy.setPlugins(sufficientPluginFollowedByFailedArray);
        TestSessionId sessionId = new TestSessionId();
        sessionId.setSessionID(Integer.valueOf(0));
        Set<Principal> authorizedPrincipals = Sets.newHashSet();
        strategy.account(sessionId,
                authorizedPrincipals);
    }

    /**
     * Failing plugin is optional in testOptionalPlugins
     * So overall authenticate should succeed
     * @throws org.dcache.gplazma.AuthenticationException
     */
    @Test
    public void testOptionalFailingConfig() throws AuthenticationException{

        AccountStrategy strategy =
                strategyFactory.newAccountStrategy();
        assertNotNull(strategy);
        strategy.setPlugins(testOptionalFailingPlugins);
        TestSessionId sessionId = new TestSessionId();
        sessionId.setSessionID(Integer.valueOf(0));
        Set<Principal> authorizedPrincipals = Sets.newHashSet();
        strategy.account(sessionId,
                authorizedPrincipals);
    }

    /**
     * The exception thrown by first required plugin is
     * thrown when the requisite plugin failure is encountered
     * Third plugin should not be executed.
     * @throws org.dcache.gplazma.AuthenticationException
     */
    @Test (expected=TestAuthenticationException.class)
    public void testRequesiteConfig1() throws AuthenticationException{

        AccountStrategy strategy =
                strategyFactory.newAccountStrategy();
        assertNotNull(strategy);
        strategy.setPlugins(testRequesitePlugins1);
        TestSessionId sessionId = new TestSessionId();
        sessionId.setSessionID(Integer.valueOf(0));
        Set<Principal> authorizedPrincipals = Sets.newHashSet();
        strategy.account(sessionId,
                authorizedPrincipals);
    }

    /**
     * The exception thrown by first required plugin is
     * thrown when the requisite plugin failure is encountered
     * Third plugin should not be executed.
     * @throws org.dcache.gplazma.TestAuthenticationException
     */
    @Test (expected=TestAuthenticationException.class)
    public void testRequesiteConfig2() throws AuthenticationException{

        AccountStrategy strategy =
                strategyFactory.newAccountStrategy();
        assertNotNull(strategy);
        strategy.setPlugins(testRequesitePlugins2);
        TestSessionId sessionId = new TestSessionId();
        sessionId.setSessionID(Integer.valueOf(0));
        Set<Principal> authorizedPrincipals = Sets.newHashSet();
        strategy.account(sessionId,
                authorizedPrincipals);
    }

    private static final class DoNothingStrategy
            implements GPlazmaAccountPlugin {

        public void account(SessionID sID,
                Set<Principal> authorizedPrincipals)
                throws AuthenticationException {
        }
    }

    private static final class AlwaysAccountStrategy
        implements GPlazmaAccountPlugin {

        public void account(SessionID sID,
                Set<Principal> authorizedPrincipals)
                throws AuthenticationException {
        }
    }

    private static final class ThrowAuthenticationExceptionStrategy
        implements GPlazmaAccountPlugin {

        public void account(SessionID sID,
                Set<Principal> authorizedPrincipals)
                throws AuthenticationException {
            throw new AuthenticationException("I always fail");
        }
    }

    private static final class ThrowTestAuthenticationExceptionStrategy
        implements GPlazmaAccountPlugin {

        public void account(SessionID sID,
                Set<Principal> authorizedPrincipals)
                throws AuthenticationException {
            throw new TestAuthenticationException("I always fail too");
        }
    }

    private static final class ThrowRuntimeExceptionStrategy
        implements GPlazmaAccountPlugin {

        public void account(SessionID sID,
                Set<Principal> authorizedPrincipals)
                throws AuthenticationException {
            throw new RuntimeException("That is what I call an exception");
        }
    }

    private static final class TestSessionId implements SessionID {

        private Comparable sessionId;
        public <T extends Comparable<? super T>> T getSessionID() {
            return (T)sessionId;
        }

        public <T extends Comparable<? super T>> void setSessionID(T sessID) {
            sessionId = sessID;
        }
    }

    private static final class TestAuthenticationException extends AuthenticationException {
        public TestAuthenticationException(String message) {
            super(message);
        }
    }

}
