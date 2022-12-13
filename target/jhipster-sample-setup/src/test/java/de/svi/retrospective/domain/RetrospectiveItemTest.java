package de.svi.retrospective.domain;

import static org.assertj.core.api.Assertions.assertThat;

import de.svi.retrospective.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RetrospectiveItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RetrospectiveItem.class);
        RetrospectiveItem retrospectiveItem1 = new RetrospectiveItem();
        retrospectiveItem1.setId(1L);
        RetrospectiveItem retrospectiveItem2 = new RetrospectiveItem();
        retrospectiveItem2.setId(retrospectiveItem1.getId());
        assertThat(retrospectiveItem1).isEqualTo(retrospectiveItem2);
        retrospectiveItem2.setId(2L);
        assertThat(retrospectiveItem1).isNotEqualTo(retrospectiveItem2);
        retrospectiveItem1.setId(null);
        assertThat(retrospectiveItem1).isNotEqualTo(retrospectiveItem2);
    }
}
