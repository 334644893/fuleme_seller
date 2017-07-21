package com.fuleme.business.posliandi.device;

import android.content.Context;

import com.fuleme.business.App;
import com.landicorp.android.eptapi.device.Printer;
import com.landicorp.android.eptapi.device.Printer.Format;
import com.landicorp.android.eptapi.exception.RequestException;

/**
 * This sample show that how to use printer.
 *
 * @author chenwei
 */
public abstract class PrinterSample extends AbstractSample {
    /**
     * Make a print progress to design the receipt.
     */
    public String short_name = "";
    public String total_fee = "";
    public String time_end = "";
    public String out_trade_no = "";
    private Printer.Progress progress = new Printer.Progress() {
        @Override
        public void doPrint(Printer printer) throws Exception {
            printer.setFormat(Format.hz(Format.HZ_SC2x2));
            printer.println(short_name+ "\n");
            Format format = new Format();
            format.setAscSize(Format.ASC_DOT5x7);
            format.setAscScale(Format.ASC_SC1x2);
            printer.setFormat(format);
			format.setAscScale(Format.ASC_SC1x1);
            printer.setFormat(Format.hz(Format.HZ_SC1x1));
			printer.setFormat(format);
            printer.printText("交易日期：" + time_end + "\n");
            printer.printText("金额: RMB " + total_fee + "\n");
            printer.printText("订单号:" + out_trade_no + "\n");
			printer.printText("\n");
//			printer.printText("---The Client Stub---\n");
			// CHS Text Format - 16x16 dot and 1 times width, 1 times height
			format.setHzScale(Format.HZ_SC1x1);
			format.setHzSize(Format.HZ_DOT16x16);
			printer.printText("---悦创未来科技有限公司---\n");
			printer.printText("\n");
//			printer.printBarCode("8799128883");

			printer.feedLine(3);
        }

        @Override
        public void onCrash() {
            onDeviceServiceCrash();
        }

        @Override
        public void onFinish(int code) {
        }

        public String getErrorDescription(int code) {
            switch (code) {
                case Printer.ERROR_PAPERENDED:
                    return "Paper-out, the operation is invalid this time";
                case Printer.ERROR_HARDERR:
                    return "Hardware fault, can not find HP signal";
                case Printer.ERROR_OVERHEAT:
                    return "Overheat";
                case Printer.ERROR_BUFOVERFLOW:
                    return "The operation buffer mode position is out of range";
                case Printer.ERROR_LOWVOL:
                    return "Low voltage protect";
                case Printer.ERROR_PAPERENDING:
                    return "Paper-out, permit the latter operation";
                case Printer.ERROR_MOTORERR:
                    return "The printer core fault (too fast or too slow)";
                case Printer.ERROR_PENOFOUND:
                    return "Automatic positioning did not find the alignment position, the paper back to its original position";
                case Printer.ERROR_PAPERJAM:
                    return "paper got jammed";
                case Printer.ERROR_NOBM:
                    return "Black mark not found";
                case Printer.ERROR_BUSY:
                    return "The printer is busy";
                case Printer.ERROR_BMBLACK:
                    return "Black label detection to black signal";
                case Printer.ERROR_WORKON:
                    return "The printer power is open";
                case Printer.ERROR_LIFTHEAD:
                    return "Printer head lift";
                case Printer.ERROR_LOWTEMP:
                    return "Low temperature protect";
            }
            return "unknown error (" + code + ")";
        }
    };

    public PrinterSample(Context context) {
        super(context);

        // Init print progress. You can also do it in 'Progress.doPrint' method,
        // but it would not be done after all step invoked.
        progress.addStep(new Printer.Step() {
            @Override
            public void doPrint(Printer printer) throws Exception {
                // Make the print method can print more than one line.
                printer.setAutoTrunc(true);
                // Default mode is real mode, now set it to virtual mode.
                printer.setMode(Printer.MODE_VIRTUAL);
            }
        });
    }

    public void startPrint() {

        try {
            progress.start();
        } catch (RequestException e) {
            e.printStackTrace();
            onDeviceServiceCrash();
        }
    }
}
