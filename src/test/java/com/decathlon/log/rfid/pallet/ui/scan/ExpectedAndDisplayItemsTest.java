package com.decathlon.log.rfid.pallet.ui.scan;

import com.decathlon.log.rfid.pallet.tdo.TdoItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by Jonathan on 23/10/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class ExpectedAndDisplayItemsTest {

    @Spy // optional : si mock de ses méthodes
    @InjectMocks
    private ExpectedAndDisplayedItems cut;

    private ItemForTableData as400MockObjectWith7DigitsItemCode;
    private ItemForTableData as400MockObjectWith6DigitsItemCode;

    @Before
    public void prepareAs400Mock(){
        // 7 digits item code (21 case)
        this.as400MockObjectWith7DigitsItemCode = new ItemForTableData("1864081", Arrays.asList(null,"","ABCD","abcdefg","123456789","1235678910111213141516","3608409918840", "2118640814029", "2118640812018","2013456081403"), 1, "Leffe");

        // 6 digits item code (200 case)
        this.as400MockObjectWith6DigitsItemCode = new ItemForTableData("186408", Arrays.asList(null,"","ABCD","abcdefg","123456789","1235678910111213141516","3608409918840", "2118640814029", "2118640812018","2013456081403"), 1, "Karmeliet");

    }

    @Test
    public void matchingEAN_ShouldReturnFalse_NullTestCase() {

        //GIVEN
        TdoItem tagMockObject = new TdoItem(null, null);

        //WHEN
        boolean result = as400MockObjectWith7DigitsItemCode.hasEan(tagMockObject.getEan());

        //THEN
        assertThat(result).isFalse();
    }

    @Test
    public void matchingEAN_ShouldReturnFalse_BlankStringTestCase(){

        //GIVEN
        TdoItem tagMockObject = new TdoItem(null,"");

        //WHEN
        boolean result = as400MockObjectWith7DigitsItemCode.hasEan(tagMockObject.getEan());

        //THEN
        Assert.assertFalse(result);
    }

    @Test
    public void matchingEAN_ShouldReturnTrue_sameEAN(){

        //GIVEN
        TdoItem tagMockObject = new TdoItem(null,"3608409918840");

        //WHEN
        boolean result = as400MockObjectWith7DigitsItemCode.hasEan(tagMockObject.getEan());

        //THEN
        Assert.assertTrue(result);
    }

    @Test
    public void compareItemCode_ShouldReturnTrue_BecauseTagMatching_21Case(){
        //GIVEN
        TdoItem tagMockObject = new TdoItem(null,"2118640814029");

        // WHEN
        Boolean result = cut.compareItemCode(tagMockObject, as400MockObjectWith7DigitsItemCode);

        //THEN
        Assert.assertTrue(result);
    }

    @Test
    public void compareItemCode_ShouldReturnTrue_BecauseTagMatching_200Case(){
        //GIVEN
        TdoItem tagMockObject = new TdoItem(null,"2001864081403");

        // WHEN
        Boolean result = cut.compareItemCode(tagMockObject, as400MockObjectWith6DigitsItemCode);

        //THEN
        Assert.assertTrue(result);
    }

    @Test
    public void compareItemCode_ShouldReturnFalse_BecauseNoTagMatching_Not200orNot21Case(){
        //GIVEN
        TdoItem tagMockObject = new TdoItem(null,"3608409918840");

        // WHEN
        Boolean result = cut.compareItemCode(tagMockObject, as400MockObjectWith7DigitsItemCode);

        //THEN
        Assert.assertFalse(result);
    }

    /*

    @Test
    public void testMatchingEANsBasedOnItemCode(){
        //GIVEN
        List<ItemForTableData> as400TestsCase = new ArrayList<ItemForTableData>();
        as400TestsCase.add(new ItemForTableData("1864081", Arrays.asList("3608409918840", "2118640814029", "2118640812018"), 1, "binouse"));
        List<TdoItem> tagsValues = new ArrayList<TdoItem>();
        tagsValues.add(new TdoItem(null,"2118640814029"));

        // WHEN
        for(ItemForTableData as400MockObjectWith7DigitsItemCode : as400TestsCase){
            for(TdoItem tagMockObject : tagsValues){
                Boolean result = cut.compareItemCode(tagMockObject,as400MockObjectWith7DigitsItemCode);
            }
        }

        //THEN

    }
*/

}
