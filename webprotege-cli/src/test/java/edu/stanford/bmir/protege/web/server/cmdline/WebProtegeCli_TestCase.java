package edu.stanford.bmir.protege.web.server.cmdline;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 7 Nov 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class WebProtegeCli_TestCase {

    private static final String CMD_NAME = "my-cmd";
    private WebProtegeCli cli;

    @Mock
    private Cmd cmd;

    @Before
    public void setUp() throws Exception {
        when(cmd.getName()).thenReturn(CMD_NAME);
        when(cmd.getHelp()).thenReturn("The help for my-cmd");
        cli = new WebProtegeCli(Collections.singletonList(cmd));
    }

    @Test
    public void shouldNotRunCommandForEmptyList() {
        cli.run(emptyList());
    }

    @Test
    public void shouldRunCommandWithEmptyArgs() {
        cli.run(Collections.singletonList(CMD_NAME));
        verify(cmd, times(1)).run(emptyList());
    }

    @Test
    public void shouldRunCommandWithArgs() {
        cli.run(asList(CMD_NAME, "arg0", "arg1"));
        verify(cmd, times(1)).run(asList("arg0", "arg1"));
    }
}
