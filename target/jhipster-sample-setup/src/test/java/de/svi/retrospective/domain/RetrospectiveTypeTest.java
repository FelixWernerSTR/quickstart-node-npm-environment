package de.svi.retrospective.domain;

import static org.assertj.core.api.Assertions.assertThat;

import de.svi.retrospective.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RetrospectiveTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RetrospectiveType.class);
        RetrospectiveType retrospectiveType1 = new RetrospectiveType();
        retrospectiveType1.setId(1L);
        RetrospectiveType retrospectiveType2 = new RetrospectiveType();
        retrospectiveType2.setId(retrospectiveType1.getId());
        assertThat(retrospectiveType1).isEqualTo(retrospectiveType2);
        retrospectiveType2.setId(2L);
        assertThat(retrospectiveType1).isNotEqualTo(retrospectiveType2);
        retrospectiveType1.setId(null);
        assertThat(retrospectiveType1).isNotEqualTo(retrospectiveType2);
    }
}
