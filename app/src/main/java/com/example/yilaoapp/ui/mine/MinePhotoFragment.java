package com.example.yilaoapp.ui.mine;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentMinePhotoBinding;
import com.example.yilaoapp.ui.mine.MineViewModel;
import com.example.yilaoapp.utils.SavePhoto;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.TipDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class MinePhotoFragment extends Fragment {


    public MinePhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentMinePhotoBinding binding;
    SavePhoto savePhoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_mine_photo,container,false);
        MineViewModel viewModel = ViewModelProviders.of(requireActivity()).get(MineViewModel.class);
        binding.setData(viewModel);

        binding.setLifecycleOwner(requireActivity());
        binding.Minedphoto.setOnClickListener(v -> {
            NavController controller = Navigation.findNavController(v);
            controller.popBackStack();
        });
        viewModel.getMinePhotoId().observe(getViewLifecycleOwner(), item -> {
            binding.Minedphoto.setImageResource(item);
        });
        binding.Minedphoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                BottomMenu.show((AppCompatActivity) requireContext(), new String[]{"保存", "取消"}, new OnMenuItemClickListener() {
                    @Override
                    public void onClick(String text, int index) {
                        //返回参数 text 即菜单名称，index 即菜单索引
                        if (index == 0) {
                            String[] PERMISSIONS = {
                                    "android.permission.READ_EXTERNAL_STORAGE",
                                    "android.permission.WRITE_EXTERNAL_STORAGE" };
                            //检测是否有写的权限
                            int permission = ContextCompat.checkSelfPermission(requireContext(),
                                    "android.permission.WRITE_EXTERNAL_STORAGE");
                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                // 没有写的权限，去申请写的权限，会弹出对话框
                                ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS,1);
                            }
                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                TipDialog.show((AppCompatActivity) getActivity(), "保存失败", TipDialog.TYPE.ERROR);
                            }else{
                                savePhoto=new SavePhoto(binding.Minedphoto,requireContext());
                                savePhoto.SaveBitmapFromView();
                                TipDialog.show((AppCompatActivity) getActivity(), "保存成功", TipDialog.TYPE.SUCCESS);
                            }
                        }
                    }
                });
                return false;
            }
        });
        return binding.getRoot();
    }
}