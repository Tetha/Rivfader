" make config
set makeprg=mvn\ -f\ /home/hk/code/rivfader/pom.xml\ compile
set errorformat=
    \%[%^[]%\\@=%f:%l:\ %m,
    \%A%[%^[]%\\@=%f:[%l\\,%v]\ %m,
    \%-Clocation\ %#:%.%#,
    \%C%[%^:]%#%m,
    \%-G%.%#,
    \%-Z\ %#

" jcommenter config
autocmd FileType java let b:jcommenter_class_author='Kalle Björklid (bjorklid@st.jyu.fi)'
autocmd FileType java let b:jcommenter_file_author='Kalle Björklid (bjorklid@st.jyu.fi)'
autocmd FileType java source ~/.vim/macros/jcommenter.vim
autocmd FileType java map <F7> :call JCommentWriter()<CR>

" keep buffers open when switching
set hidden

" write buffers on :make
set autowrite

" trim trailing whitespaces in javafiles automatically when writing
autocmd BufWrite *.java :%s/\s\+$//e

" \d turns Set<foo> bar = HashSet into ...HashSet<foo>
inoremap <Leader>d <ESC>:s/^\(.*\)\(<.*>\)\(.*\)$/\1\2\3\2/<CR>A

" F2 == :cn
map <F2> :cn<CR>

" F6 == :make
map <F6> :make<CR>
