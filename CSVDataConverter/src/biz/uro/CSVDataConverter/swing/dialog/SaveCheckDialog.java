package biz.uro.CSVDataConverter.swing.dialog;


@SuppressWarnings("serial")
public class SaveCheckDialog extends MessageDialog {

	public interface ISaveCheckDialogProcess {
		void saveProcess();
		void process();
	}
	
	public SaveCheckDialog( final ISaveCheckDialogProcess process ) {
		super( "保管されていない変更があります。続行しますか？" );
		addDefaultButton( new DialogButton( "保存して続行" ) {
			@Override
			public void onPush() {
				process.saveProcess();
				process.process();
			}
		});
		addButton( new DialogButton( "保存しないで続行" ) {
			@Override
			public void onPush() {
				process.process();
			}
		});
		addButton( new DialogButton( "キャンセル" ) {
			@Override
			public void onPush() {
			}
		});
	}

}
