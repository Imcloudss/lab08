package it.unibo.deathnote;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.IllegalArgumentException;

import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.impl.*;

class TestDeathNote {

   private static final int ILLEGAL_INDEX = 0; // try also with < 1 and > RULE.size()
   private static final String MARIO = "Mario";
   private static final String LUIGI = "Luigi";

   private DeathNoteImpl deathNote;

   @BeforeEach
   public void setUp() {
      this.deathNote = new DeathNoteImpl();
   }

   @Test
   public void testIllegalRule() {
      try {
         this.deathNote.getRule(ILLEGAL_INDEX);
         Assertions.fail();
      } catch (IllegalArgumentException iAE) {
         Assertions.assertEquals("This rule doesn't exist", iAE.getMessage());
      }
   }

   @Test
   public void testBlankOrNullRule() {
      for(final String rule : DeathNote.RULES) {
         assertNotNull(rule);
         assertFalse(rule.isBlank());
      }
   }

   @Test
   public void testWritingHumans() {
      Assertions.assertFalse(this.deathNote.isNameWritten(MARIO));
      this.deathNote.writeName(MARIO);
      Assertions.assertTrue(this.deathNote.isNameWritten(MARIO));
      Assertions.assertFalse(this.deathNote.isNameWritten(LUIGI));
      Assertions.assertFalse(this.deathNote.isNameWritten(""));
   }

   @Test
   public void testCauseOfDeath() throws InterruptedException {

      try {
         this.deathNote.writeDeathCause("eating red poisonous mushrooms");
         Assertions.fail();
      } catch (IllegalStateException iSE) {
         Assertions.assertEquals("The name has to be written before the cause of death", iSE.getMessage());
      }

      this.deathNote.writeName(MARIO);
      assertEquals("heart attack", this.deathNote.getDeathCause(MARIO));

      this.deathNote.writeName(LUIGI);
      assertTrue(this.deathNote.writeDeathCause("karting accident"));
      assertEquals("karting accident", this.deathNote.getDeathCause(LUIGI));

      Thread.sleep(100);

      Assertions.assertFalse(this.deathNote.writeDeathCause("drowning"));
      Assertions.assertEquals("karting accident", this.deathNote.getDeathCause(LUIGI));      
   }

   @Test
   public void testDetailsOfDeath() throws InterruptedException {

      try {
         this.deathNote.writeDetails("eating red poisonous mushrooms");
         Assertions.fail();
      } catch (IllegalStateException iSE) {
         Assertions.assertEquals("The name has to be written before the details of death", iSE.getMessage());
      }

      this.deathNote.writeName(MARIO);
      assertEquals("", this.deathNote.getDeathDetails(MARIO));
      assertTrue(this.deathNote.writeDetails("ran for too long"));
      assertEquals("ran for too long", this.deathNote.getDeathDetails(MARIO));
      
      this.deathNote.writeName(LUIGI);
      Thread.sleep(6100);
      Assertions.assertFalse(this.deathNote.writeDetails("drowning"));
      Assertions.assertEquals("", this.deathNote.getDeathDetails(LUIGI));      
   }

}