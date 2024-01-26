package com.example.dndcharactersheet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dndcharactersheet.R;
import com.example.dndcharactersheet.models.Character;

import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {
    private List<Character> characterList;
    private OnItemClickListener listener;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }

    public CharacterAdapter(List<Character> characterList, Context context) {
        this.characterList = characterList;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_character, parent, false);
        return new CharacterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder holder, int position) {
        Character currentCharacter = characterList.get(position);
        String characterInfo;
        characterInfo = String.format("<font color ='#FFFFFF'><b>%s</b> - <i>%s %s, Level %d </i>",
                currentCharacter.getName(),
                currentCharacter.getRace(),
                currentCharacter.getCharacterClass(),
                currentCharacter.getLevel());
        holder.textViewCharacterInfo.setText(HtmlCompat.fromHtml(characterInfo, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }

    class CharacterViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCharacterInfo;

        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCharacterInfo = itemView.findViewById(R.id.textViewCharacterInfo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    if(listener != null && getAdapterPosition() != RecyclerView.NO_POSITION){
                        listener.onItemLongClick(getAdapterPosition());
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}
