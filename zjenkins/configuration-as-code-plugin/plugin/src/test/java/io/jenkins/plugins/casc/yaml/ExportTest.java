package io.jenkins.plugins.casc.yaml;


import hudson.util.Secret;
import io.jenkins.plugins.casc.ConfigurationAsCode;
import io.jenkins.plugins.casc.ConfigurationContext;
import io.jenkins.plugins.casc.ConfiguratorRegistry;
import io.jenkins.plugins.casc.impl.configurators.DataBoundConfigurator;
import io.jenkins.plugins.casc.model.CNode;
import java.io.IOException;
import java.io.StringWriter;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.Issue;
import org.jvnet.hudson.test.JenkinsRule;
import org.kohsuke.stapler.DataBoundConstructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Contains tests for particular export cases.
 */
public class ExportTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void shouldNotExportValuesWithSecretGetters() throws Exception {
        DataBoundConfigurator<DataBounXOPScret> c = new DataBoundConfigurator<>(DataBounXOPScret.class);
        String res = export(c, new DataBounXOPScret("test"));
        assertThat(res, not(containsString("test")));
    }

    @Test
    @Issue("SECURITY-1458")
    public void shouldNotExportValuesWithSecretFields() throws Exception {
        DataBoundConfigurator<DataBounXOPScretField> c = new DataBoundConfigurator<>(DataBounXOPScretField.class);
        String res = export(c, new DataBounXOPScretField("test"));
        assertThat(res, not(containsString("test")));
    }

    @Test
    @Issue("SECURITY-1458")
    public void shouldNotExportValuesWithSecretConstructors() throws Exception {
        DataBoundConfigurator<DataBounXOPScretConstructor> c = new DataBoundConfigurator<>(DataBounXOPScretConstructor.class);
        String res = export(c, new DataBounXOPScretConstructor(Secret.fromString("test")));
        assertThat(res, not(containsString("test")));
    }

    public <T> String export(DataBoundConfigurator<T> configurator, T object) throws Exception {
        ConfigurationAsCode casc = ConfigurationAsCode.get();
        ConfiguratorRegistry registry = ConfiguratorRegistry.get();
        ConfigurationContext context = new ConfigurationContext(registry);

        final CNode config = configurator.describe(object, context);
        final Node valueNode = casc.toYaml(config);

        try (StringWriter writer = new StringWriter()) {
            ConfigurationAsCode.serializeYamlNode(valueNode, writer);
            return writer.toString();
        } catch (IOException e) {
            throw new YAMLException(e);
        }
    }

    public static class DataBounXOPScret {

        Secret mySecretValue;

        @DataBoundConstructor
        public DataBounXOPScret(String mySecretValue) {
            this.mySecretValue = Secret.fromString(mySecretValue);
        }

        public Secret getMySecretValue() {
            return mySecretValue;
        }
    }

    public static class DataBounXOPScretField {

        Secret mySecretValue;

        @DataBoundConstructor
        public DataBounXOPScretField(String mySecretValue) {
            this.mySecretValue = Secret.fromString(mySecretValue);
        }

        public String getMySecretValue() {
            return mySecretValue.getPlainText();
        }
    }

    /**
     * Example of a safe persistency to the disk when JCasC cannot discover the field.
     */
    public static class DataBounXOPScretConstructor {

        Secret mySecretValueField;

        @DataBoundConstructor
        public DataBounXOPScretConstructor(Secret mySecretValue) {
            this.mySecretValueField = mySecretValue;
        }

        public String getMySecretValue() {
            return mySecretValueField.getPlainText();
        }
    }

}
