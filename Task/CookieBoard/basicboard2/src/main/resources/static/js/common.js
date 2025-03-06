/**
 * 사용자 로그인 상태를 확인하는 함수.
 * - Local Storage에 저장된 액세스 토큰이 없으면 로그인 페이지로 리디렉션.
 */
let checkToken = () => {
    let token = localStorage.getItem('accessToken'); // Local Storage에서 JWT 토큰 가져오기

    // 토큰이 없거나 공백이면 로그인 페이지로 이동
    if (token == null || token.trim() === '') {
        window.location.href = '/member/login'; // 로그인 페이지로 리디렉션
    }
};
