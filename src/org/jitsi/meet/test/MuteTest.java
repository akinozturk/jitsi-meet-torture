/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jitsi.meet.test;

import junit.framework.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;

/**
 * Mutes and unmutes tests.
 * @author Damian Minkov
 */
public class MuteTest
    extends TestCase
{
    /**
     * Constructs test
     * @param name the method name for the test.
     */
    public MuteTest(String name)
    {
        super(name);
    }

    /**
     * Orders the tests.
     * @return the suite with order tests.
     */
    public static junit.framework.Test suite()
    {
        TestSuite suite = new TestSuite();

        suite.addTest(new MuteTest("muteFocusAndCheck"));
        suite.addTest(new MuteTest("unMuteFocusAndCheck"));
        suite.addTest(new MuteTest("muteParticipantAndCheck"));
        suite.addTest(new MuteTest("unMuteParticipantAndCheck"));
        suite.addTest(new MuteTest("focusMutesParticipantAndCheck"));
        suite.addTest(new MuteTest(
                    "participantUnMutesAfterFocusMutedHimAndCheck"));
        suite.addTest(new MuteTest(
                    "muteFocusBeforeSecondParticipantJoins"));

        return suite;
    }

    /**
     * Mutes the focus and checks at other participant is this is visible.
     */
    public void muteFocusAndCheck()
    {
        TestUtils.clickOnToolbarButton(ConferenceFixture.getFocus(), "mute");

        TestUtils.waitsForElementByXPath(
            ConferenceFixture.getSecondParticipant(),
            "//span[@class='audioMuted']/i[@class='icon-mic-disabled']", 5);

    }

    /**
     * Unmutes focus and checks at other participant is this is visible.
     */
    public void unMuteFocusAndCheck()
    {
        TestUtils.clickOnToolbarButton(ConferenceFixture.getFocus(), "mute");

        TestUtils.waitsForElementNotPresentByXPath(
            ConferenceFixture.getSecondParticipant(),
            "//span[@class='audioMuted']/i[@class='icon-mic-disabled']", 5);
    }

    /**
     * Mutes the participant and checks at focus side.
     */
    public void muteParticipantAndCheck()
    {
        TestUtils.clickOnToolbarButton(
            ConferenceFixture.getSecondParticipant(), "mute");

        TestUtils.waitsForElementByXPath(
            ConferenceFixture.getFocus(),
            "//span[@class='audioMuted']/i[@class='icon-mic-disabled']", 5);
    }

    /**
     * UnMutes the participant and checks at focus side.
     */
    public void unMuteParticipantAndCheck()
    {
        TestUtils.clickOnToolbarButton(
            ConferenceFixture.getSecondParticipant(), "mute");

        TestUtils.waitsForElementNotPresentByXPath(
            ConferenceFixture.getFocus(),
            "//span[@class='audioMuted']/i[@class='icon-mic-disabled']", 5);
    }

    /**
     * Finds the menu that can be used by the focus to control the participant.
     * Hovers over it. Finds the mute link and mute it.
     * Then checks in the second participant page whether it is muted
     */
    public void focusMutesParticipantAndCheck()
    {
        WebElement elem = ConferenceFixture.getFocus().findElement(By.xpath(
            "//span[@class='remotevideomenu']/i[@class='fa fa-angle-down']"));

        Actions action = new Actions(ConferenceFixture.getFocus());
        action.moveToElement(elem);
        action.perform();

        TestUtils.waitsForDisplayedElementByXPath(
            ConferenceFixture.getFocus(),
            "//ul[@class='popupmenu']/li/a[@class='mutelink']",
            5);

        ConferenceFixture.getFocus().findElement(
                By.xpath("//ul[@class='popupmenu']/li/a[@class='mutelink']"))
            .click();

        // and now check whether second participant is muted
        TestUtils.waitsForElementByXPath(
            ConferenceFixture.getSecondParticipant(),
            "//span[@class='audioMuted']/i[@class='icon-mic-disabled']", 5);

        action.release();
    }

    /**
     * UnMutes once again the second participant and checks in the focus page
     * does this change is reflected.
     */
    public void participantUnMutesAfterFocusMutedHimAndCheck()
    {
        TestUtils.clickOnToolbarButton(
            ConferenceFixture.getSecondParticipant(), "mute");

        TestUtils.waitsForElementNotPresentByXPath(
            ConferenceFixture.getFocus(),
            "//span[@class='audioMuted']/i[@class='icon-mic-disabled']", 5);

        // lets give time to the ui to reflect the change in the ui of the focus
        TestUtils.waits(1000);
    }

    /**
     * Closes the participant and leaves the focus alone in the room.
     * Mutes the focus and then joins new participant and checks the status
     * of the mute icon.
     * At the end unmutes to clear the state.
     */
    public void muteFocusBeforeSecondParticipantJoins()
    {
        ConferenceFixture.quit(ConferenceFixture.getSecondParticipant());

        // just in case wait
        TestUtils.waits(1000);

        TestUtils.clickOnToolbarButton(ConferenceFixture.getFocus(), "mute");

        ConferenceFixture.startParticipant();

        ConferenceFixture.checkParticipantToJoinRoom(
            ConferenceFixture.getSecondParticipant(), 10);

        ConferenceFixture.waitsParticipantToJoinConference(
            ConferenceFixture.getSecondParticipant());

        TestUtils.waitsForElementByXPath(
            ConferenceFixture.getSecondParticipant(),
            "//span[@class='audioMuted']/i[@class='icon-mic-disabled']", 5);

        // now lets unmute
        unMuteFocusAndCheck();
    }
}