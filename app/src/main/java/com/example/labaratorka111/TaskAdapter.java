package com.example.labaratorka111;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private final List<Task> taskList;
    private final OnTaskCheckedListener onTaskCheckedListener;
    private final OnTaskDeletedListener onTaskDeletedListener;

    public interface OnTaskCheckedListener {
        void onTaskChecked(int position, boolean isChecked);
    }

    public interface OnTaskDeletedListener {
        void onTaskDeleted(int position);
    }

    public TaskAdapter(List<Task> taskList, OnTaskCheckedListener onTaskCheckedListener,
                       OnTaskDeletedListener onTaskDeletedListener) {
        this.taskList = taskList;
        this.onTaskCheckedListener = onTaskCheckedListener;
        this.onTaskDeletedListener = onTaskDeletedListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.textViewTask.setText(task.getTitle());

        // Временно удаляем слушателя, чтобы избежать циклов обновления
        holder.checkBoxCompleted.setOnCheckedChangeListener(null);
        holder.checkBoxCompleted.setChecked(task.isCompleted());

        // Зачёркиваем текст, если задача выполнена
        if (task.isCompleted()) {
            holder.textViewTask.setPaintFlags(holder.textViewTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.textViewTask.setPaintFlags(holder.textViewTask.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        // Переустанавливаем слушателя после установки состояния
        final int currentPosition = position;
        holder.checkBoxCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (onTaskCheckedListener != null) {
                onTaskCheckedListener.onTaskChecked(currentPosition, isChecked);
            }
        });

        // Обработка удаления
        holder.btnDelete.setOnClickListener(v -> {
            if (onTaskDeletedListener != null) {
                onTaskDeletedListener.onTaskDeleted(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxCompleted;
        TextView textViewTask;
        ImageButton btnDelete;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxCompleted = itemView.findViewById(R.id.checkBoxCompleted);
            textViewTask = itemView.findViewById(R.id.textViewTask);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
