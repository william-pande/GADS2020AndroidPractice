package ug.r.gadsleadershipmobileapplication.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import java.io.Serializable;

import ug.r.gadsleadershipmobileapplication.R;


public class MyDialogFragment extends DialogFragment {

    @Nullable
    private Confirm confirm;
    private String message;
    private DialogType dialogType;

    public MyDialogFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null) {
            if (this.getArguments().containsKey("confirm")) {
                this.confirm = (Confirm) this.getArguments().getSerializable("confirm");
            }
            this.message = this.getArguments().getString("message");
            this.dialogType = (DialogType) this.getArguments().getSerializable("dialogType");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ug.r.gadsleadershipmobileapplication.databinding.FragmentMyDialogBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_dialog, container, false);

        binding.buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyDialogFragment.this.confirm != null) {
                    MyDialogFragment.this.confirm.confirm(true);
                    MyDialogFragment.this.dismiss();
                }
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyDialogFragment.this.confirm != null) {
                    MyDialogFragment.this.confirm.confirm(false);
                    MyDialogFragment.this.dismiss();
                }
            }
        });

        if (this.dialogType == DialogType.CONFIRM && this.confirm != null) {
            binding.layoutCancel.setVisibility(View.VISIBLE);
            binding.icon.setVisibility(View.GONE);
        } else if (this.dialogType == DialogType.ERROR) {
            binding.icon.setBackgroundResource(R.drawable.icon_error);
        } else if (this.dialogType == DialogType.SUCCESS) {
            binding.icon.setBackgroundResource(R.drawable.icon_successful);
        } else {
            this.dismiss();
        }

        binding.message.setText(this.message);

        return binding.getRoot();
    }

    public static void newInstance(DialogType dialogType, String message, @Nullable Confirm confirm, FragmentActivity activity) {
        MyDialogFragment dialog = new MyDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("dialogType", dialogType);
        if (confirm != null) {
            args.putSerializable("confirm", confirm);
        }
        args.putString("message", message);
        dialog.setArguments(args);

        dialog.show(activity.getSupportFragmentManager(), "my_dialog");
    }

    public enum DialogType {ERROR, CONFIRM, SUCCESS}

    public interface Confirm extends Serializable {
        void confirm(boolean confirmed);
    }
}